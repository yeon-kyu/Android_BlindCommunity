# BlindCommunity
 android application
 
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
HomeActivity 안에 UpperFragment, HomeScreenFragment, LookupScreenFragment, MyTraceScreenFragment, WritePostFragment, lowerFragment이 포함되며,
JsonTaskModel은 AsyncTask 클래스를 상속받아 http통신을 처리합니다. 다시 JsonTaskModel을 상속하는 클래스들은 override를 통해 각자의 상황에 맞게 통신을 합니다.  </br>
 </br>
 </br>
 
 
<로그인 화면>
<div>
<img width = "300" src = "https://github.com/yeon-kyu/Android_application_BlindCommunity/blob/main/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/%EB%A1%9C%EA%B7%B8%EC%9D%B8.jpg">
 </div>
처음 앱을 켰을 때 화면인 로그인 화면입니다. </br>
 </br>
 </br>
