package kr.co.sist.e_learning.usercourse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.admin.account.AdminAccountController;
import kr.co.sist.e_learning.course.CourseDTO;
import kr.co.sist.e_learning.course.CourseService;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.quiz.QuizService;
import kr.co.sist.e_learning.video.VideoDTO;
import kr.co.sist.e_learning.video.VideoService;

@Controller
public class UserCourseController {

    private final AdminAccountController adminAccountController;

	@Autowired
	private UserCourseService ucs;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private VideoService vs;
	
	@Autowired
	private QuizService qs;

    UserCourseController(AdminAccountController adminAccountController) {
        this.adminAccountController = adminAccountController;
    }
	
	@GetMapping("/ui/user_lecture")
	public String userLecture(@RequestParam("seq") String courseSeq, Model model,
			 Authentication authentication) {
		
		System.out.println("강의 시퀀스 ㅅㅂ "+courseSeq);
		
		
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		System.out.println("강의실 들어온 사람 seq "+userSeq);
		
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		
		
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
//		List<QuizListDTO> quizList = qs.searchQuizByCourseSeq(courseSeq);
		List<QuizListDTO> quizSeq = qs.searchQuizSeqByCoursSEq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
		
		model.addAttribute("courseData", cDTO);
		model.addAttribute("videoList", videoList);
		model.addAttribute("quizList", quizList);
		return "ui/user_lecture";
	}
	
	@GetMapping("/ui/user_course")
	public String userCourse() {
		
		
		return "ui/user_course";
	}
	
	@PostMapping("/user/course_enroll")
	public ResponseEntity<?> registerCourse(@RequestParam("courseSeq") String courseSeq,
	                                        Authentication authentication) {
	    Object principal = authentication.getPrincipal();
	    Long userSeq = null;

	    if (principal instanceof Long) {
	        userSeq = (Long) principal;
	    }

	    System.err.println("🔥🔥🔥 받은 userSeq: " + userSeq);
	    System.out.println("강의 시퀀스 : " + courseSeq);
	    System.out.println("강의 페이지에 들어온 유저시퀀스 : " + userSeq);

	    try {
	        // 강의를 만든 사람의 userSeq 조회
	        CourseDTO cDTO = cs.selectUserSeqByCourseSeq(courseSeq);
	        System.out.println("강의만든 사람 userSeq : " + cDTO.getUserSeq());
	        
	        if (cDTO.getUserSeq() == null) {
	            return ResponseEntity.badRequest().body(Map.of("msg", "강의 정보가 없습니다."));
	        }

	        // 1. 자기가 만든 강의는 수강 불가
	        if (cDTO.getUserSeq().equals(userSeq)) {
	            return ResponseEntity.badRequest().body(Map.of("msg", "자신이 만든 강의는 수강할 수 없습니다."));
	        }

	        // 2. 이미 수강 중인지 확인
//	        boolean alreadyEnrolled = ucs.checkUserAlreadyEnrolled(courseSeq, userSeq); // 추가 필요
	        int alreadyEnrolled = ucs.selectAlreadyEnrollCourse(courseSeq);
	        if (alreadyEnrolled > 0) {
	            return ResponseEntity.badRequest().body(Map.of("msg", "이미 수강 중인 강의입니다."));
	        }

	        // 3. 수강 등록
	        UserCourseDTO ucDTO = new UserCourseDTO();
	        ucDTO.setCourseSeq(courseSeq);
	        ucDTO.setUserSeq(userSeq);

	        int result = ucs.addUserCourse(ucDTO);
	        System.out.println("인서트 성공 여부 : " + result);

	        if (result == 0) {
	            return ResponseEntity.internalServerError().body(Map.of("msg", "수강 등록에 실패했습니다."));
	        }

	        return ResponseEntity.ok(Map.of("msg", "수강완료"));

	    } catch (Exception e) {
	        System.err.println("🔥 예외 발생: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body(Map.of("msg", "서버 오류 발생"));
	    }
	}
//	@PostMapping("/user/course_enroll")
//	public ResponseEntity<?> registerCourse(@RequestParam("courseSeq") String courseSeq,
//			Authentication authentication){
//		Object principal = authentication.getPrincipal();
//		Long userSeq = null;
//		if(principal instanceof Long) {
//			userSeq = (Long) principal;
//		}
//		System.err.println("🔥🔥🔥 받은 userSeq: " + userSeq);
//		
//		System.out.println("강의 시퀀스 : "+courseSeq);
//		System.out.println("유저시퀀스 : "+userSeq);
//		//강의 만든사람 userSeq검색
//		
//		try {
//			CourseDTO cDTO = cs.selectUserSeqByCourseSeq(courseSeq);
//			UserCourseDTO ucDTO = new UserCourseDTO();
//			ucDTO.setCourseSeq(courseSeq);
//			ucDTO.setUserSeq(userSeq);
//			
//			if (cDTO.getUserSeq().equals(userSeq)) {
//	            return ResponseEntity.badRequest().body(Map.of("msg", "자신이 만든 강의는 수강할 수 없습니다."));
//	        }else {
//		    int result = ucs.addUserCourse(ucDTO);
//		    System.out.println("인서트형 된거임? : "+result);
//		    if(result==0) {
//		        System.out.println("실패");
//		    }
//			}
//		} catch (Exception e) {
//		    System.err.println("🔥 예외 발생: " + e.getMessage());
//		    e.printStackTrace();  // 정확한 오류 위치 확인
//		}
//		
//		
//		
//		return ResponseEntity.ok(Map.of("msg","수강완료"));
//		
//	}
	
	
	@GetMapping("/user/showUserCourses")
	@ResponseBody
	public Map<String, Object> showUserCourse(Authentication authentication,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int limit){
		System.out.println("showUserCourses 진입");
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		System.out.println(userSeq);
		
		
		int offset = (page -1)*limit;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userSeq);
		param.put("offset", offset);
		param.put("limit", limit);
		System.out.println("showUserCourses 진입2");
		List<UserCourseDTO> paginationList = ucs.searchUserCourseByPage(param);
		System.out.println("showUserCourses 진입3");
		
		int totalCount = ucs.searchUserCourseCount(userSeq);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userCourseList", paginationList);
		result.put("totalCount", totalCount);
		result.put("page", page);
	    result.put("limit", limit);
		
		List<UserCourseDTO> list = ucs.searchUserCourseByUserId(userSeq);
		for(UserCourseDTO uDTO : list) {
			System.out.println(uDTO.toString());
		}
		
		result.put("courses", list);
		return result;
	}
	
	@GetMapping("/ui/user_registered_course")
	public String goToRegisterdCourse(@RequestParam("seq") String courseSeq, Model model,
			 Authentication authentication) {
		
		System.out.println("강의 시퀀스 tlqkf "+courseSeq);
	
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		
		
		
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		System.out.println("courseDTO : "+cDTO.toString());
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
//		List<QuizListDTO> quizList = qs.searchQuizByCourseSeq(courseSeq);
		List<QuizListDTO> quizSeq = qs.searchQuizSeqByCoursSEq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
		
		
		
		model.addAttribute("courseData", cDTO);
		model.addAttribute("videoList", videoList);
		model.addAttribute("quizList", quizList);
		return "ui/user_registered_course";
	}
	
	
    @GetMapping("/courses")
    public String listPublicCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "uploadDate,desc") String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            Model model) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("sort", sort);
        params.put("category", category);
        params.put("difficulty", difficulty);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        PageResponseDTO<UserCourseListDisplayDTO> responseDTO = ucs.getPublicCourses(params);

        model.addAttribute("courseList", responseDTO.getList());
        model.addAttribute("currentPage", responseDTO.getPage());
        model.addAttribute("totalPages", responseDTO.getTotalPages());
        model.addAttribute("startPage", responseDTO.getStartPage());
        model.addAttribute("endPage", responseDTO.getEndPage());
        model.addAttribute("totalCount", responseDTO.getTotalCnt());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);
        model.addAttribute("category", category);
        model.addAttribute("difficulty", difficulty);

        return "user/course/course_list";
    }
}

