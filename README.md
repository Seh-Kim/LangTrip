# 🌍 LangTrip - 외국어 학습 특화 이러닝 플랫폼

<img width="1024" height="1024" alt="KakaoTalk_20250728_213153547" src="https://github.com/user-attachments/assets/d8e0d13c-aa7b-43c8-a294-f34caa1018db" />


<br/>

## 📑 목차  
- [📝 프로젝트 소개](#-프로젝트-소개)
- [🛠 기술 스택](#-기술-스택)
- [💾 ERD](#-erd)
- [✨ 주요 기능 소개](#-주요-기능-소개)  
- [🎬 시연영상](#-시연영상)   
- [💁‍♂️ 팀원 소개](#-팀원-소개)  

<br/>

## 📝 프로젝트 소개
**LangTrip**은 외국어 학습에 특화된 웹 기반 이러닝 플랫폼입니다.  
누구나 강의자가 되어 강의를 개설하고, 다른 사용자로부터 후원을 받아 수익을 창출할 수 있습니다.  
쌍방향 커뮤니티, 강의 추천, 질문 게시판, 후원 시스템 등을 통해 학습자 중심의 열린 학습 환경을 제공합니다.

- **개발 기간**: 2025.06.25 ~ 2025.07.29 (5주)
- **팀 구성**: 9명  
- **아키텍처**: Spring Boot + Thymeleaf 기반 MVC 웹 애플리케이션  

<br/>

## 🛠 기술 스택

### Backend
- **Language**: Java (JDK 17)
- **Framework**: Spring Boot 3.x (JWT 기반 인증)
- **Database**: Oracle 11g
- **ORM**: MyBatis
- **Build Tool**: Maven
- **CI/CD**: Jenkins
- **Infra**: Docker, Kubernetes, AWS EC2

### Frontend
- **Template**: Thymeleaf, Bootstrap
- **Scripting**: JavaScript, jQuery, Axios, Chart.js

### Tools
- **IDE**: Eclipse IDE
- **Version Control**: Git, GitHub
- **Data Tool**: SQL Developer, Excel, Notion, OpenCSV
- **OS**: Windows 11, Ubuntu, VirtualBox

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"> 
<img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"> 
<img src="https://img.shields.io/badge/mybatis-000000?style=for-the-badge&logo=mybatis&logoColor=white"> 
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> 
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> 
<img src="https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white">

<br/>

## 💾 ERD
> ERD는 추후 업데이트 예정입니다.  

<br/>

## ✨ 주요 기능 소개

### 👥 사용자 기능
- 🎓 강의 등록 및 수강 (일반 사용자도 강의 개설 가능)
- 💰 후원 기능 (사용자 간 후원/후원받기 가능)
- 🗣 커뮤니티 (강의 추천, 자유 게시판, 댓글, 신고 등)
- 🧑‍💻 마이페이지: 정보 수정, 탈퇴, 수강 목록, 후원 내역 등
- 📋 지원센터: FAQ, 공지사항, 1:1 질문 등록
- 🪙 정산 시스템: 계좌 연동, 정산/환불 신청
- 📊 광고 클릭 및 통계 반영

### 🛠 관리자 기능
- 🧾 통계 대시보드 (가입/탈퇴, 접속/유입경로, 광고클릭 등)
- 📚 강의 관리 (비공개/삭제, 검색, 필터링)
- 🧑 회원 관리 (정보 확인, 정지/탈퇴 처리)
- 💬 커뮤니티 관리 (신고, 삭제, 필터링)
- 💸 후원/정산 관리 (내역 확인, 승인/거절)
- 📢 공지사항/FAQ/1:1문의 등록 및 답변 처리
- 📥 CSV 다운로드 기능 (가입/탈퇴, 광고 클릭 통계 등)

<br/>

## 🎬 시연영상
> 시연 영상은 추후 업로드 예정입니다.

<br/>

## 💁‍♂️ 팀원 소개

| 이름 | 역할 | 담당 업무 |
|------|------|-----------|
| **김민진** | 팀장 | DBA, 마이페이지, 프로필/정보 수정, 회원탈퇴, 대시보드 통계 csv |
| **김세형** | 부팀장 | 메인화면, 관리자 회원가입/로그인, 계정관리, 로그관리 |
| **정성재** | 팀원 | 회원가입/로그인, 인증 토큰, 소셜 연동, 후원 결제/정산 관리 |
| **심규민** | 팀원 | 강의 등록/진행, 영상 업로드, 난이도 설정, 사용자 수강 기능 |
| **박선은** | 팀원 | 퀴즈 등록/연동/결과 저장 처리, 영상 편집 |
| **최승재** | 팀원 | 커뮤니티 작성/수정/삭제, 파일 업로드, 추천/조회수 처리 |
| **이장훈** | 팀원 | FAQ, 질문 등록, 키워드 검색, 관리자 답변 관리 |
| **정제균** | 팀원 | 커뮤니티 신고/사유, 신고글 처리, 회원 신고 필터링 |

<br/>

---

**© 2025 LangTrip Project Team. 본 프로젝트는 교육 목적으로 개발되었습니다.**

> 🙋 README 작성: 김세형
