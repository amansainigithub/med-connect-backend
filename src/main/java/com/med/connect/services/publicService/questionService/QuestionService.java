package com.med.connect.services.publicService.questionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.connect.bucket.bucketModels.BucketModel;
import com.med.connect.bucket.bucketService.BucketService;
import com.med.connect.domain.User;
import com.med.connect.domain.questionDomain.Questions;
import com.med.connect.repository.UserRepository;
import com.med.connect.repository.questionRepo.QuestionRepo;
import com.med.connect.services.publicService.questionService.impl.QuestionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
    private UserRepository userRepository;

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
                log.info("File :: {} " + file);
                //SAVE IMAGE TO S3-BUCKET
                BucketModel bucketModel =  bucketService.uploadFile(file);

                questions.setByteSize(String.valueOf(file.getBytes()));
                questions.setFileUrl(bucketModel.getBucketUrl());
                questions.setFileName(file.getOriginalFilename());
                questions.setFileSize(String.valueOf(file.getSize()));
                questions.setContentType(file.getContentType());

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
    public   List<Map<Object ,Object>> getQuestions(int page) {
       List<Map<Object ,Object>> data = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
            Page<Questions> questions =  this.questionRepo.findAll(pageable);

            List<Questions> lists = questions.getContent();
            for(Questions ques : lists)
            {
                //EveryTime Create a Object of Map [Object, object]
                Map<Object,Object> node = new HashMap<>();
                node.put("firstName", ques.getUser().getFirstname());
                node.put("email", ques.getUser().getEmail());
                node.put("profilePicture", ques.getUser().getProfilePicUrl());
                node.put("note", ques.getUser().getNote());

                //Add Question To List With Single add User Object Also
                node.put( "questionNode" , ques );

                LocalDateTime now = LocalDateTime.now();

                //Get Hours , Month and Seconds
                LocalDateTime creationDate =  ques.getCreationDate();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                 this.find_difference_hours_minutes_seconds(
                                dtf.format(creationDate).replace("/","-").toString() ,
                                dtf.format(now).replace("/","-").toString() , node );

                //Get Year , Month , Days
                LocalDateTime localDateTime = ques.getCreationDate();
                LocalDate start_date = LocalDate.of( localDateTime.getYear() ,
                                        localDateTime.getMonth() ,
                                        localDateTime.getDayOfMonth() );

                LocalDate end_date = LocalDate.of(now.getYear(),now.getMonth(),now.getDayOfMonth());
                this.find_difference_year_month_days( start_date , end_date);

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
}
