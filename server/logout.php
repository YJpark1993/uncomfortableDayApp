<?php
// 상기꺼
  session_start();
  if(isset($_COOKIE['userID']) || isset($_COOKIE['userPW'])){
    $loginID = trim($_COOKIE['userID']);
    $loginPW = trim($_COOKIE['userPW']);

    setcookies('userID', '' , time()-3600, '/' );
    setcookie('userPW', '' , time()-3600, '/' );

}
  

?>
