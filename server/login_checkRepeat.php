<?php
// 유진이꺼 
$conn = mysqli_connect('localhost', 'root', 'djsxhr12');
mysqli_select_db($conn, 'app');
mysqli_set_charset($conn,"UTF8");

$email = $_POST['email'];

$result = mysqli_query($conn, "SELECT * FROM user_info WHERE user_email='$email'");

while($row = mysqli_fetch_array($result, MYSQL_BOTH)) {
	printf("repeat");
}
?>
