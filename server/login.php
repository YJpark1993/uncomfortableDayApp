<?php
//유진이꺼
$email = $_POST['email'];
$pwd = $_POST['pwd'];
$name = $_POST['name'];
$phone = $_POST['phone'];


$conn = mysqli_connect('localhost', 'root', 'djsxhr12');
mysqli_select_db($conn, 'app');
$sql = 'INSERT INTO user_info (user_email, user_pw, user_name, user_phone) VALUES ("'.$email.'","'.$pwd.'","'.$name.'","'.$phone.'")';
mysqli_set_charset($conn,"UTF8");
mysqli_query($conn,$sql);
?>
