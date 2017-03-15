<?php

$conn = mysqli_connect('localhost', 'root', 'djsxhr12');
mysqli_select_db($conn, 'app');
mysqli_set_charset($conn,"UTF8");

$res  = mysqli_query($conn, "SELECT * FROM context_info");

$result = array();
$str = "123";

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('latitude'=>$row[3],'longitude'=>$row[4]));
}


?>

<script>
var str = '<?= $str ?>';
var arr = <?= json_encode($result) ?>;

console.log(str); // hello
console.log(arr); // ["my","friend"]
</script>