package kr.co.sist.e_learning.community.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.sist.e_learning.community.dto.PageDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;
import kr.co.sist.e_learning.community.dto.UsersssDTO;
import kr.co.sist.e_learning.community.service.CommunityPostService;
import kr.co.sist.e_learning.community.service.VoteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/csj")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityService;
    @Value("${file.upload-dir.community}")
    private String uploadDir;
    
    @Value("${upload.path.community}")
    private String uploadPath;

    @Autowired
    private VoteService voteService;
    
    

    @GetMapping("/community")
    public String list(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "50") int size,
        @RequestParam(name = "tab", defaultValue = "all") String tab,
        @RequestParam(name = "keyword", required = false) String keyword,
        Model model
    ) {
        int offset = (page - 1) * size;

        List<CommunityPostDTO> postList;
        int totalCount;

        if ("best".equals(tab)) {
            postList = communityService.getBestPosts(offset, size);
            totalCount = communityService.getBestPostCount();
            model.addAttribute("bestPostCount", totalCount); 
        } else {
            postList = communityService.getPostsPaginatedWithSearch(offset, size, keyword);
            totalCount = communityService.getTotalPostCountWithSearch(keyword);
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("tab", tab);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPostCount", totalCount);

        return "csj/community";
    }

    @GetMapping("/communityWrite")
    public String communityWrite(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            UsersssDTO fakeUser = new UsersssDTO();
            fakeUser.setUserSeq(400);
            fakeUser.setEmail("jj000808@naver.com");
            fakeUser.setNickname("asd22");
            session.setAttribute("loginUser", fakeUser);
        }
        return "csj/communityWrite";
    }

    @PostMapping("/writeOk")
    public String writePost(CommunityPostDTO dto) {
        communityService.writeRecommendation(dto);
        return "redirect:/csj/community";
    }

    @GetMapping("/community/detail")
    public String detail(@RequestParam("postId") Long postId, Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            UsersssDTO fakeUser = new UsersssDTO();
            fakeUser.setUserSeq(400);
            fakeUser.setEmail("jj000808@naver.com");
            fakeUser.setNickname("asd22");
            session.setAttribute("loginUser", fakeUser);
        }
        communityService.increaseViewCount(postId);

        CommunityPostDTO post = communityService.getRecommendation(postId);
        List<CommunityCommentDTO> comments = communityService.getAllComments(postId);

        // 🔥 추천 수 조회 추가
        int upCount = voteService.getVoteCount(postId.intValue(), "UP");
        int downCount = voteService.getVoteCount(postId.intValue(), "DOWN");

        model.addAttribute("post", post);
        model.addAttribute("commentList", comments);
        model.addAttribute("upCount", upCount);
        model.addAttribute("downCount", downCount);

        return "csj/communityDetail";
    }
    
    
//댓글
    @GetMapping("/comment/add")
    public String commentAdd(@RequestParam("postId") Long postId, Model model) {
    	CommunityPostDTO post = communityService.getRecommendation(postId);
    	model.addAttribute("post", post);
    	return "csj/communityDetail";
    }
//ㅅㅂ fuck shit asssshole

  
    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);

            // 이 URL이 실제로 접근 가능해짐
            return "/images/community/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/community/delete")
    public String deletePost(@RequestParam("postId") Long postId, HttpSession session) {
        // 권한 체크: 로그인한 유저와 작성자가 같을 때만 삭제 허용
        UsersssDTO loginUser = (UsersssDTO) session.getAttribute("loginUser");

        CommunityPostDTO post = communityService.getRecommendation(postId);
        if (loginUser != null && loginUser.getUserSeq().equals(post.getUserId())) {
            communityService.deletePost(postId);  // 삭제 처리
        }
        return "redirect:/csj/community";
    }
////////////좆댓구알////////////////////////////////////////
    
    @PostMapping("/comment/write")
    @ResponseBody
    public CommunityCommentDTO writeComment(@RequestBody CommunityCommentDTO commentDTO, HttpSession session) {

        UsersssDTO loginUser = (UsersssDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        // 유저 정보 주입
        commentDTO.setUserId2(loginUser.getUserSeq());
        commentDTO.setNickname(loginUser.getNickname());

        // 방어 코드
        if (commentDTO.getPostId2() == null || commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("postId2 또는 content가 비어 있음");
        }


        communityService.writeCommet(commentDTO);
        commentDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return commentDTO;
    }
    
    @GetMapping("/csj/community")
    public String showCommunity(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String keyword,
            Model model) {

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);
        pageDTO.setKeyword(keyword);

        List<CommunityPostDTO> postList = communityService.getPostList(pageDTO);
        int totalCount = communityService.getPostCount(pageDTO);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int)Math.ceil((double)totalCount / size));
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "csj/community";
    }
    
    
    
    
    
   
}

