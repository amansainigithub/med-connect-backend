package com.med.connect.services.publicService.questionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.connect.bucket.bucketModels.BucketModel;
import com.med.connect.bucket.bucketService.BucketService;
import com.med.connect.domain.User;
import com.med.connect.domain.questionDomain.QuestionFiles;
import com.med.connect.domain.questionDomain.Questions;
import com.med.connect.domain.questionDomain.VotingUpAndDownInfo;
import com.med.connect.exception.QuestionIdNotFoundException;
import com.med.connect.repository.UserRepository;
import com.med.connect.repository.questionRepo.QuestionFilesRepo;
import com.med.connect.repository.questionRepo.QuestionRepo;
import com.med.connect.repository.votingRepo.VotingUpAndDownRepo;
import com.med.connect.services.publicService.questionService.impl.QuestionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QuestionService implements QuestionServiceImpl {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuestionFilesRepo questionFilesRepo;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VotingUpAndDownRepo votingUpAndDownRepo;

//    @Autowired
//    private QuestionFilesRepo questionFilesRepo;

    @Override
    public Questions addQuestionService(MultipartFile file, String jsonNode) {
        try {
            Map<Object,Object> mapper =  new ObjectMapper().readValue(jsonNode , LinkedHashMap.class);
            mapper.forEach((k,v)->{
                log.info("Key mapper :: " + k);
                log.info("Value mapper :: " + v);
            });

            User user = this.userRepository.findById(Long.parseLong(String.valueOf(mapper.get("userId")))).get();

            //Creating Med Post Object and Set Data To Object
            Questions questions = new Questions();

            //Checking Post Type
            checkingPostType(file, String.valueOf(mapper.get("content")), questions);

            //Check if File is Empty
            if(file != null)
            {
                //log.info("File :: {} " + file);
                //SAVE IMAGE TO S3-BUCKET
//                BucketModel bucketModel =  bucketService.uploadFile(file);
//
//                ArrayList<QuestionFiles> questionFileList = new ArrayList<>();
//                QuestionFiles questionFiles = new QuestionFiles();
//
//                questionFiles.setByteSize(String.valueOf(file.getBytes()));
//                questionFiles.setFileUrl(bucketModel.getBucketUrl());
//                questionFiles.setFileName(file.getOriginalFilename());
//                questionFiles.setFileSize(String.valueOf(file.getSize()));
//                questionFiles.setContentType(file.getContentType());
//                questionFileList.add(questionFiles);

//                double kilobytes = (file.getBytes().length / 1024);
//                System.out.println("KB :: " + kilobytes);
            }

            questions.setUser(user);
            questions.setTitle(String.valueOf(mapper.get("title")));
            questions.setContent(String.valueOf(mapper.get("content")));

            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth();
            questions.setQuestionDay(String.valueOf(currentDay));

            Month currentMonth = currentDate.getMonth();
            questions.setQuestionMonth(String.valueOf(currentMonth));

            int currentYear = currentDate.getYear();
            questions.setQuestionYear(String.valueOf(currentYear));

            questions.setQuestionDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            questions.setQuestionTime(String.valueOf(java.time.LocalTime.now()));

            Questions response = this.questionRepo.save(questions);


            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }


    @Override
    public Questions questionImages(long questionId, MultipartFile file) {
        try {
            Optional<Questions> optional = this.questionRepo.findById(questionId);
            if( optional.isPresent() )
            {
                Questions questions = optional.get();

                //SAVE IMAGE TO S3-BUCKET
                BucketModel bucketModel =  bucketService.uploadFile(file);

                ArrayList<QuestionFiles> questionFileList = new ArrayList<>();

                QuestionFiles questionFiles = new QuestionFiles();
                questionFiles.setByteSize(String.valueOf(file.getBytes()));
                questionFiles.setFileUrl(bucketModel.getBucketUrl());
                questionFiles.setFileName(file.getOriginalFilename());
                questionFiles.setFileSize(String.valueOf(file.getSize()));
                questionFiles.setContentType(file.getContentType());
                questionFiles.setQuestionId(questionId);
                questionFiles.setQuestions(questions);

                questionFileList.add(questionFiles);
                questions.setQuestionFiles(questionFileList);

                this.questionRepo.save(questions);
                log.info( " file update success " + file.getOriginalFilename() +
                          " Question Id :: {} " + questionId);

                return questions;
            }
            else {
                throw new QuestionIdNotFoundException(" Question Id Not Found here  :: {} " + questionId);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error( e.getMessage() );
        }
        return  null;
    }



    @Override
    public   List<Map<Object ,Object>> getQuestions(int page) {
       List<Map<Object ,Object>> data = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
            Page<Questions> questions =  this.questionRepo.findAll(pageable);

            List<Questions> lists = questions.getContent();

            //Current Date
            LocalDate now = LocalDate.now();
            for(Questions ques : lists)
            {
                //EveryTime Create Object of Map [Object, object]
                Map<Object,Object> node = new HashMap<>();
                node.put("firstName", ques.getUser().getFirstname());
                node.put("email", ques.getUser().getEmail());
                node.put("profilePicture", ques.getUser().getProfilePicUrl());
                node.put("note", ques.getUser().getNote());

                if(ques.getViews() != null)
                    node.put("views",withSuffix(ques.getViews()));
                else
                    node.put("views",0);


                //Add Question To List With Single add User Object Also
                node.put( "questionNode" , ques );


                LocalDateTime localDateTime = ques.getCreationDate();
                this.timeDuration(ques.getCreationDate() , now , node);


                //Get Hours , Month and Seconds
//                LocalDateTime creationDate =  ques.getCreationDate();
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                 this.find_difference_hours_minutes_seconds(
//                                dtf.format(creationDate).replace("/","-").toString() ,
//                                dtf.format(now).replace("/","-").toString() , node );

                //Get Year , Month , Days
//                LocalDateTime localDateTime = ques.getCreationDate();
//                LocalDate start_date = LocalDate.of( localDateTime.getYear() ,
//                                        localDateTime.getMonth() ,
//                                        localDateTime.getDayOfMonth() );

//                LocalDate end_date = LocalDate.of(now.getYear(),now.getMonth(),now.getDayOfMonth());
//                this.find_difference_year_month_days( start_date , end_date);

                //Vote Up Counter
                Long voteUpCount = votingUpAndDownRepo.countFromQuestionIdAndVote(String.valueOf(ques.getId()) ,"U");
                node.put("voteUpCount" , withSuffix( voteUpCount ) );

                //Vote Down Counter
                Long voteDownCount = votingUpAndDownRepo.countFromQuestionIdAndVote(String.valueOf(ques.getId()) ,"D");
                node.put("voteDownCount" , withSuffix( voteDownCount ) );

                //Vote Checker
                Optional<VotingUpAndDownInfo> voteChecker =  votingUpAndDownRepo.
                                                    findByUserIdAndQuestionId(
                                                            String.valueOf(ques.getUser().getId()),
                                                            String.valueOf(ques.getId()));
                if(voteChecker.isPresent())
                {
                    VotingUpAndDownInfo votingUpAndDownInfo = voteChecker.get();
                    if(votingUpAndDownInfo.getIsVoter().equals(Boolean.TRUE)){
                            node.put("voteResult" , votingUpAndDownInfo.getVote());
                    }
                }else {
                    node.put("voteResult" ,null);
                }

                data.add(node);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error("Something Went Wrong No Data Found Here !!!");
        }
        return data;
    }

    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }

    private void timeDuration(LocalDateTime startDate ,
                              LocalDate now ,
                              Map<Object , Object> node)
    {
        LocalDate start_date = LocalDate.of( startDate.getYear() ,
                                            startDate.getMonth() ,
                                            startDate.getDayOfMonth() );
        Period diff = Period.between( start_date , now );
        System.out.printf("\nDifference is %d years, %d months and %d days old\n\n",
                            diff.getYears(), diff.getMonths(), diff.getDays());

        if ( diff.getYears() == 0  )
        {
            if( diff.getMonths() == 0)
            {
                node.put( "timer" , diff.getDays() +" days ago");
            }
            else if ( diff.getMonths() != 0 )
            {
                node.put( "timer" ,diff.getMonths() + " months " + diff.getDays() +" days ago");
            }
        }else {
            node.put( "timer" ,  diff.getYears() +" year " +
                        diff.getMonths() + " months " +
                        diff.getDays() +" days");
        }
    }

     private int year = 0;
     private int month = 0;
     private int days = 0;
    private void find_difference_year_month_days(LocalDate start_date, LocalDate end_date)
    {
        // find the period between
        // the start and end date
        Period diff
                = Period
                .between(start_date,
                        end_date);

        // Print the date difference
        // in years, months, and days
//        System.out.print(
//                "Difference "
//                        + "between two dates is: ");
//
//        // Print the result
//        System.out.println( " years :" + diff.getYears() +
//                            " months : " + diff.getMonths() +
//                            " Days : "+diff.getDays());

        year = diff.getYears();
        month = diff.getMonths();
        days = diff.getDays();
    }


    private void find_difference_hours_minutes_seconds(
                               String start_date,
                               String end_date ,
                               Map<Object,Object> node )
    {
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Try Class
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            //Calculate time difference
            // in milliseconds
            long difference_In_Time = d2.getTime() - d1.getTime();

            //Calculate time difference in seconds,
            // minutes, hours, years, and days
            long difference_In_Seconds
                                        = TimeUnit.MILLISECONDS
                                        .toSeconds(difference_In_Time)
                                        % 60;

            long difference_In_Minutes
                                        = TimeUnit
                                        .MILLISECONDS
                                        .toMinutes(difference_In_Time)
                                        % 60;

            long difference_In_Hours
                                        = TimeUnit
                                        .MILLISECONDS
                                        .toHours(difference_In_Time)
                                        % 24;

            long difference_In_Days
                                        = TimeUnit
                                        .MILLISECONDS
                                        .toDays(difference_In_Time)
                                        % 365;

            long difference_In_Years
                                        = TimeUnit
                                        .MILLISECONDS
                                        .toDays(difference_In_Time)
                                        / 365l;

            // Print the date difference in
            // years, in days, in hours, in
//            // minutes, and in seconds
//            System.out.print(
//                    "Difference"
//                            + " between two dates is: ");

            // Print result
//            System.out.println(
//                                + difference_In_Hours
//                                + " hours, "
//                                + difference_In_Minutes
//                                + " minutes, "
//                                + difference_In_Seconds
//                                + " seconds");

            if(year == 0 && month == 0  && days == 0)
            {
                if(difference_In_Seconds != 0 )
                {
                    node.put("timer" ,
                                    difference_In_Minutes + " min " +
                                    difference_In_Seconds + " sec ago");
                }
                else {
                    node.put("timer" ,
                                    difference_In_Hours + " hours " +
                                    difference_In_Minutes + " min ago");
                }
            }
            else {
                if(days != 0)
                {
                    node.put("timer" , days + " days ago ");
                }
                else
                {
                    node.put("timer" , year + "years " + month +" month ");
                }

            }


//            if ( difference_In_Hours == 0 && difference_In_Minutes == 0  && difference_In_Seconds != 0 )
//            {
//                node.put("timer" ,  difference_In_Seconds +
//                                    " seconds ago");
//            }
//            else if ( difference_In_Hours == 0 && difference_In_Minutes != 0)
//            {
//                node.put("timer" , difference_In_Minutes + " minutes "
//                                    + difference_In_Seconds + " seconds ago");
//            }
//            else if(difference_In_Hours != 0){
//                node.put("timer" ,  difference_In_Hours +
//                        " hours "+  difference_In_Minutes + " minutes");
//            }
//            else if (year == 0 && month == 0 && days !=0)
//            {
//                node.put("timer" , days + " days ago");
//            }
//            else if (year == 0 && month != 0 )
//            {
//                node.put("timer" , month + " month " + days + " days ago");
//            }
//            else if (year != 0)
//            {
//                node.put("timer" , year + " years" + month + " month ago");
//            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void checkingPostType(MultipartFile multipartFile , String content, Questions questions)
    {
        //Only Image Conditions
        if(multipartFile == null)
        {
            //Only Content Conditions
            questions.setQuestionType("C");
        } else if (content.isEmpty() || content == null || content == "null") { //Only Image Conditions
            //Only Image
            questions.setQuestionType("I");
        }
        else {
            //Content + Image Conditions
            questions.setQuestionType("C + I");
        }
    }


    @Override
    public Map<Object , Object> questionViewsService(Long questionId) {
        Map<Object , Object > node = new HashMap();
        Questions questions = null;
        try {
              Optional<Questions> optional =   this.questionRepo.findById( questionId );
              if(optional.isPresent())
              {
                questions =  optional.get();
                if(questions.getViews() == null)
                   questions.setViews( 1l );
                else
                    questions.setViews(questions.getViews() + 1);

                  this.questionRepo.save(questions);
                  log.error("Question View Counter Update Success :: questionId" + questionId);
              }
              else {
                  throw new QuestionIdNotFoundException("Question Id Not Found " + questionId);
              }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error("Question Views Counter Not Updated !! ");
        }

        node.put("question" , questions);
        List<QuestionFiles> questionFilesList =  this.questionFilesRepo.
                                                        findByQuestionId(questions.getId());
        node.put("questionFiles",questionFilesList);

        this.timeDuration(questions.getCreationDate() , LocalDate.now() , node );
       return node;
    }



}
