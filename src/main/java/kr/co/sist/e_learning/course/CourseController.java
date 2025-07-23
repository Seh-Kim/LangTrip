package kr.co.sist.e_learning.course;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.quiz.QuizService;
import kr.co.sist.e_learning.video.VideoDTO;
import kr.co.sist.e_learning.video.VideoService;

@Controller
public class CourseController {

	@Value("${upload.saveDir2}")
	private String saveDir2;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private VideoService vs;

	@Autowired
	private QuizService qs;
	
//	@GetMapping("ui/instroductor_course")
//	public String myLecture(HttpSession session) {
//		
//		if(session.getAttribute("userSeq") == null) {
//			session.setAttribute("userSeq", "test123");
//		}
//		
//		
//		
//		return "ui/instroductor_course";
//	}
	
	
	
	
	
	
	@GetMapping("/ui/my_lecture")
	public ResponseEntity<?> instroductorPage(Authentication authentication,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "4") int limit) {
		
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long)principal;
		}
		
		int offset = (page - 1)*limit;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userSeq", userSeq);
		param.put("offset", offset);
		param.put("limit", limit);
		
		List<CourseDTO> courseList = cs.selectCourseByPage(param);
		int totalCount = cs.selectCourseCount(userSeq);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("courseList", courseList);
	    result.put("totalCount", totalCount);
	    result.put("page", page);
	    result.put("limit", limit);
	    result.put("msg", "pagination");
		
		return ResponseEntity.ok(result);
	}

	
	@PostMapping("/ui/register_course")
	public ResponseEntity<?> registerCourse(@RequestParam("thumbnail") MultipartFile mf,
			@ModelAttribute CourseDTO cDTO,
			Authentication authentication)throws Exception{
		System.out.println("아 되냐? 여기까진?");
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		System.out.println("사용자 seq : "  + userSeq);
		
		String thumbNail="";
		
		if(mf.getContentType().startsWith("image") && !mf.isEmpty()) {
			int maxSize=1024*1024*5;
//			if(mf.getSize()>maxSize) {
//				throw new Exception("업로드한 파일의 크기는 최대 5MByte까지 입니다.");
//			}//end if
			String originalFileName = mf.getOriginalFilename();
			
			File dir = new File(saveDir2);
			if(!dir.exists()) {
				dir.mkdirs();
			}//end if
			
			File uploadFile = new File(saveDir2+File.separator+originalFileName);
			String fileName = "";
			String fileExt = "";
			int fileSeperator=originalFileName.lastIndexOf(".");
			fileName = originalFileName.substring(0, fileSeperator);
			if(originalFileName.contains(".")) {
				fileExt = originalFileName.substring(fileSeperator+1);
			}
			
			int index = 1;
//			while(uploadFile.exists()) {
//				String newName = fileName+"("+index+")."+fileExt;
//				uploadFile = new File(saveDir2+File.separator+newName);
//				index++;
//			}
			mf.transferTo(uploadFile);
			thumbNail=uploadFile.getName();
		
		
		}
//		String userSeq = (String)session.getAttribute("userSeq");
//		cDTO.setUserSeq(userSeq);
		cDTO.setUserSeq(userSeq);
		
		cDTO.setThumbnailName(thumbNail);
		cDTO.setThumbnailPath("/upload/img/"+thumbNail);
		cDTO.setUploadDate(new Date());
		System.out.println("썸네일: " + cDTO.getThumbnailName());
		System.out.println("유저 아이디 : "+cDTO.getUserSeq());
		System.out.println("썸네일 패스 : " + cDTO.getThumbnailPath());
		
		System.out.println("카테고리:" + cDTO.getCategory());
		System.out.println("난이도:" + cDTO.getDifficulty());
		System.out.println("제목: " + cDTO.getCourseTitle());
		System.out.println("설명: " + cDTO.getIntroduction());
		cDTO.setFlag("T");
		int result = cs.addCourse(cDTO);
		if (result == 0) {
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                         .body(Map.of("msg", "DB 삽입 실패"));
		}
		
		return ResponseEntity.ok(Map.of("msg", "영상 등록 완료", "courseData", cDTO));
	}
	
	
	@GetMapping("/upload/upload_course")
	public String showCourseForm(@RequestParam("seq") String courseSeq, Model model) {
		System.out.println("📌 강의 이동 요청 수신: " + courseSeq);
		
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
		QuizListDTO qlDTO = new QuizListDTO();
		
		
//		List<QuizListDTO> quizList = qs.searchQuizByCourseSeq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
		
		
			System.out.println(cDTO.getUserSeq()+"님의 "+cDTO.getUserSeq());
			System.out.println(cDTO.getUserSeq()+"님의 "+cDTO.getCourseTitle());
			System.out.println(cDTO.getUserSeq()+"님의 "+cDTO.getDifficulty());
			System.out.println(cDTO.getUserSeq()+"님의 "+cDTO.getUploadDate());
			System.out.println(cDTO.getUserSeq()+"님의 "+cDTO.getCourseSeq());
			System.out.println("-------------------------------------------");
		
			if(videoList.isEmpty()) {
				System.out.println("비디오 강의들이 없음요");
			}else {
			for(VideoDTO vDTO : videoList) {
				
				System.out.println(cDTO.getUserSeq()+"님의 "+vDTO.getType());
				System.out.println(cDTO.getUserSeq()+"님의 "+vDTO.getFileName());
				System.out.println(cDTO.getUserSeq()+"님의 "+vDTO.getCourseSeq());
				}
			}
		model.addAttribute("courseData", cDTO);
		model.addAttribute("quizList", quizList);
		model.addAttribute("videoList", videoList);
		return "ui/upload_frm";
	}
		
	@GetMapping("/upload/upload_update")
	public String updateCourse(HttpSession session, Model model) {
		CourseDTO cDTO = (CourseDTO) session.getAttribute("courseToEdit");
		System.out.println("수정 강의 시퀀스 : "+cDTO.getCourseSeq());
		System.out.println("수정 강의 제목 : "+cDTO.getCourseTitle());
		System.out.println("수정 강의 설명 : "+cDTO.getIntroduction());
		System.out.println("수정 강의 난이도 : "+cDTO.getDifficulty());
		System.out.println("수정 강의 카테고리 : "+cDTO.getCategory());
		System.out.println("수정 강의 전 썸네일 이름 : "+cDTO.getThumbnailName());
		System.out.println("수정 강의 전 경로 : "+cDTO.getThumbnailPath());
		model.addAttribute("cDTO", cDTO);
		return "ui/update_course";
	}
	
	@PostMapping("/upload/storeCourseSession")
	@ResponseBody
	public void storeCourseSession(@RequestBody CourseDTO cDTO, HttpSession session) {
		session.setAttribute("courseToEdit", cDTO);
	}
	
	@PostMapping("/upload/modify_course")
	public String modifyCourse(@RequestParam("thumbnail") MultipartFile mf
			,@ModelAttribute CourseDTO cDTO, Model model, HttpSession session) throws Exception{
		String thumbNail="";
		if(mf.getContentType().startsWith("image") && !mf.isEmpty()) {
			int maxSize=1024*1024*5;
			if(mf.getSize()>maxSize) {
				model.addAttribute("msg", "다시 이미지 넣으셈");
				throw new Exception("업로드한 파일의 크기는 최대 5MByte까지 입니다.");
			
			}//end if
			String originalFileName = mf.getOriginalFilename();
			File dir = new File(saveDir2);
			File uploadFile = new File(saveDir2+File.separator+originalFileName);
			String fileName = "";
			String fileExt = "";
			int fileSeperator=originalFileName.lastIndexOf(".");
			fileName = originalFileName.substring(0, fileSeperator);
			if(originalFileName.contains(".")) {
				fileExt = originalFileName.substring(fileSeperator+1);
			}
			mf.transferTo(uploadFile);
			thumbNail=uploadFile.getName();

		}
		cDTO.setThumbnailName(thumbNail);
		cDTO.setThumbnailPath("/upload/img/"+thumbNail);
		
		System.out.println("썸네일 이름"+cDTO.getThumbnailName());
		
		int result = cs.modifyCourse(cDTO);
		if(result>0) {
			System.out.println("수정 성공시발새꺄");
		}else {
			System.out.println("수정 실패새꺄");
		}
		
		System.out.println("수정 강의 후 시퀀스 : "+cDTO.getCourseSeq());
		System.out.println("수정 강의 후 제목 : "+cDTO.getCourseTitle());
		System.out.println("수정 강의 후 설명 : "+cDTO.getIntroduction());
		System.out.println("수정 강의 후 난이도 : "+cDTO.getDifficulty());
		System.out.println("수정 강의 후 카테고리 : "+cDTO.getCategory());
		System.out.println("수정 강의 후 썸네일 이름 : "+cDTO.getThumbnailName());
		System.out.println("수정 강의 후 경로 : "+cDTO.getThumbnailPath());
//		session.setAttribute("courseToEdit", cDTO);
		model.addAttribute("cDTO", cDTO);
		 
		
		return "ui/update_course";
	}
	
	@PostMapping("/upload/delete_course")
	@ResponseBody
	public String deleteCourse(@RequestParam("seq")String courseSeq, Model model) {
		int result = cs.removeCourse(courseSeq);
		String msg="";
		if(result>0) {
			msg ="success";
			System.out.println("삭제 성공");
		}else {
			msg ="fail";
			System.out.println("삭제 실패");
		}
		
		return msg;
	}
	

}
