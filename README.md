# BlindCommunity
 android application
 
 ##구글 플레이스토어 링크
 https://play.google.com/store/apps/details?id=com.yeonkyu.blindcommunity
 
  </br>
 </br>
 개요 : 취업준비생들이 정보를 공유하고 소통할 수 있는 커뮤니티입니다. 로그인을 하면 누구나 게시물을 작성하고 게시물에 댓글을 작성할 수 있습니다.  
 </br>
 백앤드는 AWS EC2 환경에서 nodejs로 구현되어 있으며 MySQL와 연동됩니다. 앱과 서버는 http 통신을 합니다.
  </br>
 </br>
 
 <앱 전체 시스템 구조>
<div>
<img width = "600" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png">
 </div>
  </br>
HomeActivity 안에 UpperFragment, HomeScreenFragment, LookupScreenFragment, MyTraceScreenFragment, WritePostFragment, lowerFragment이 포함되며,
JsonTaskModel은 AsyncTask 클래스를 상속받아 http통신을 처리합니다. 다시 JsonTaskModel을 상속하는 클래스들은 override를 통해 각자의 상황에 맞게 통신을 합니다.  </br>
 </br>
 </br>
 
 
<로그인 화면>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%A1%9C%EA%B7%B8%EC%9D%B8.jpg">
 </div>
처음 앱을 켰을 때 화면인 로그인 화면입니다. 로그인할 때 "자동 로그인" 체크박스에 체크하면 다음 번 접속할 때 로그인과정이 자동으로 이루어집니다.</br>
 </br>
 </br>

<로그인 입력 화면>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%A1%9C%EA%B7%B8%EC%9D%B8%EC%9E%85%EB%A0%A5.jpg">
 </div>
로그인화면에서 아이디와 비밀번호를 입력했을 때 화면입니다. </br>
 </br>
 </br>
 
 <회원가입 화면>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85.jpg">
 </div>
계정을 만들기 위해 회원가입 버튼을 눌렀을 때의 화면입니다. </br>
 </br>
 </br>
 
 <홈 화면>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%92%A4%EB%A1%9C%EA%B0%80%EA%B8%B01%EB%B2%88%ED%81%B4%EB%A6%AD.jpg">
 </div>
로그인한 뒤의 홈화면입니다. 여기서 '뒤로가기'를 한번 누르면 한번 더 눌러야 앱이 종료된다고 Toast 메세지가 뜹니다 </br>
하단의 fragment는 왼쪽부터 '로그아웃', '홈화면으로 돌아가기', '내가 쓴 게시물 보기' 입니다.</br>
 </br>
 </br>
 
  <자유게시판 화면>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EC%9E%90%EC%9C%A0%EA%B2%8C%EC%8B%9C%ED%8C%90lookup.jpg">
 </div>
홈화면에서 자유게시판을 선택하여 들어왔을 때 최근 게시물부터 차례대로 보여주는 화면입니다. 가장 최근의 20개의 게시물을 먼저 보여주고, 추가로 보고 싶으면 하단의 "더 보기"버튼을 누를 때마다 20개씩 추가로 보여집니다. </br>
우측 하단의 "글쓰기"를 클릭하면 게시물을 작성할 수 있습니다.</br>
 </br>
 </br>

  <게시물 내부>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EA%B2%8C%EC%8B%9C%ED%8C%90inside.jpg">
 </div>
자유게시판(또는 정보/취업 게시판)에서 게시물을 클릭하여 내부에 들어왔을 때의 화면입니다. 게시물의 작성자가 본인이면 화면 상단에 "삭제"버튼이 보이게 됩니다.
댓글을 보여주는 리스트는 CustomListView로 구현하였습니다. 댓글을 작성하면 리스트의 최하단에 업로드됩니다.</br>
 </br>
 </br>
 
  <댓글 삭제>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%8C%93%EA%B8%80%EC%82%AD%EC%A0%9C.jpg">
 </div>
댓글 리스트를 클릭했을 때 댓글의 작성자이면 위와같이 댓글을 삭제할 것인지 다시 묻는 다이얼로그가 생성됩니다. 삭제를 누르면 서버에서 댓글이 삭제됩니다.</br>
 </br>
 </br>
   <댓글 삭제 이후>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%8C%93%EA%B8%80%EC%82%AD%EC%A0%9C%EC%9D%B4%ED%9B%84.jpg">
 </div>
댓글이 삭제된 이후의 화면입니다.</br>
 </br>
 </br>
 
  <게시물 삭제>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EA%B2%8C%EC%8B%9C%EB%AC%BC%EC%82%AD%EC%A0%9C.jpg">
 </div>
게시물의 작성자가 본인일 때 보이는 "삭제" 버튼을 클릭하면 삭제할 것인지 다시 묻는 다이얼로그가 생성되고 삭제를 할 수 있습니다.</br>
 </br>
 </br>
 
   <내가 쓴 게시물 보기>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%82%B4%EA%B0%80%EC%93%B4%EA%B2%8C%EC%8B%9C%EB%AC%BC.jpg">
 </div>
본인이 작성한 게시물을 모아서 볼 수 있습니다. 리스트를 클릭하면 해당 게시물로 이동합니다.</br>
 </br>
 </br>
 
  <로그아웃하기>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%A1%9C%EA%B7%B8%EC%95%84%EC%9B%83diaglog.jpg">
 </div>
현재 계정에서 로그아웃하여 다시 로그인창으로 이동할 수 있습니다.</br>
 </br>
 </br>
