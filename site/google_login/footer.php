<?php

error_reporting(E_ALL);
ini_set("display_errors", 1);

//session_start();
require_once 'db_connection.php';
//mysqli_report(MYSQLI_REPORT_ALL);
//mysqli_report(MYSQLI_REPORT_ERROR);

if(!isset($_SESSION['logat'])) $_SESSION['logat']='';
if(!isset($_SESSION['admin'])) $_SESSION['admin']='';
if(!isset($_SESSION['org_unit'])) $_SESSION['org_unit']='';
if(!isset($_SESSION['login_id'])) $_SESSION['login_id']='';
if(!isset($_SESSION['full_name'])) $_SESSION['full_name']='';
if(!isset($_SESSION['email'])) $_SESSION['email']='';
if(!isset($_SESSION['profile_pic'])) $_SESSION['profile_pic']='';
if(!isset($_GET['opt'])) $_GET['opt']='';
//if(!isset($_POST['activ'])) $_POST['activ'] = '';
//if(!isset($_POST['nume'])) $_POST['nume'] = '';
//if(!isset($_POST['email'])) $_POST['email'] = '';

//print "aa".$_SESSION['logat']."login=".$_SESSION['login_id'];

if($_SESSION['logat'] != "Adevarat" || $_SESSION['login_id'] ==''){
//if($_SESSION['logat'] != "Adevarat" ){
    echo "Trebuie sa fi autentificat ca si diriginte! <br>";
    echo '<br>Redirectionare la <a class="login-btn" href="login.php">Login</a>
    <p id="wait"></p>';
   print' 
    <script>
    var div = document.getElementById(\'wait\');
    var counter = 0;
    var waitSecs = 8;
    var refreshId = setInterval(function () {
      ++counter;
      div.innerHTML = div.innerHTML + \'&#x25cf;\'
      if(counter == waitSecs){
        clearInterval(refreshId);
        top.location.href=\'login.php\'
      }
    }, 1000);
    </script>
    ';
    //header('Location: login.php');
    //exit;
}else{

    print '<div class="_container">
    <div class="_img">
        <img src="'.$_SESSION['profile_pic'].'" alt="'.$_SESSION['full_name'].'">
    </div>
    <div class="_info">
        <h1>'.$_SESSION['full_name'].'</h1>
        <p>'.$_SESSION['email'].'</p>
        <a href="logout.php">Logout</a>
    </div>
</div>
</div>';
print '</body>
</html>';
}
?>