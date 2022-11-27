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
if(!isset($_SESSION['cnp'])) $_SESSION['cnp']='';
if(!isset($_SESSION['rang'])) $_SESSION['rang']='';
if(!isset($_SESSION['nivel'])) $_SESSION['nivel']='';
if(!isset($_SESSION['clasa'])) $_SESSION['clasa']='';
if(!isset($_SESSION['org_unit_google'])) $_SESSION['org_unit_google']='';

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
include "head.php";

require_once 'google-api-php-client--PHP7.4/vendor/autoload.php';
$key_file_location = '/var/php_keys/php-login-365307-69f13304118c.json';
$delegatedAdmin = 'admin@lmvineu.ro';
$scopes = array(
    'https://www.googleapis.com/auth/admin.directory.user',
    'https://www.googleapis.com/auth/admin.directory.orgunit',
    'https://www.googleapis.com/auth/admin.directory.group'
);

$client = new Google_Client();
$client->setApplicationName("This is the name");
$client->setAuthConfig($key_file_location);
$client->setSubject($delegatedAdmin);
$client->setScopes($scopes);

$client->setAccessType('offline');
//$client->setPrompt('select_account consent');

$service_users = new Google\Service\Directory($client);

$user_elev=$_SESSION['email'];
//$user_elev="test.pass@lmvineu.ro";
//$user_elev="marius.crainic@lmvineu.ro";
//$user_elev="darius.neamt@lmvineu.ro";
$user = $service_users->users->get($user_elev);
 
  
  //print_r($buff);
  $nivel='';
  $clasa='';
  $rang='';
  $cnp='';
  $org_unit=$user->getorgUnitPath();
  $org_unit2 = get_string_between($org_unit, '/', '/');
  //print $org_unit2;
  if($org_unit2=="Elevi" || $org_unit2=="Profeso")
        {
              $val=$user->getExternalIds()[0];
              $cnp = get_string_between($val['value'], '[', ']');
              $rang = get_string_between($val['value'], '(', ')');
              if($rang == "elev")
              {
                  $nivel = get_string_between($org_unit, '/Elevi/', '/');
                  $clasa = substr($org_unit, strrpos($org_unit, "/") + 1);
                  
              }
        }else {
          $rang="Altul(admins)";
        }
  
    $_SESSION['org_unit_google']=$org_unit;
    $_SESSION['cnp']=$cnp;
    $_SESSION['rang']=$rang;
    $_SESSION['nivel']=$nivel;
    $_SESSION['clasa']=$clasa;
    
  /*
   print_r($org_unit);
  print "<br>";
  print_r($cnp);
  print "<br>";
  print_r($rang);
  print "<br>";
  print_r($nivel);
  print "<br>";
  print_r($clasa);
  */
if($_SESSION['rang']=='elev'){
  include "menu_elev.php";
  print '<div class="content">';
  print 'Esti logat cu:';
  print '<table>
              <tr>
                  <td>Tip</td>
                  <td>'.$_SESSION['rang'].'</td>
              </tr>
              <tr>
                  <td>Email</td>
                  <td>'.$user_elev.'</td>
              </tr>   
              <tr>
                  <td>Nivel</td>
                  <td>'.$_SESSION['nivel'].'</td>
              </tr>
              <tr>
                  <td>Clasa</td>
                  <td>'.$_SESSION['clasa'].'</td>
              </tr>
        </table>';
    }else if($_SESSION['rang']=='prof'){
      include "menu_prof.php";
      print '<div class="content">';
      print 'Esti logat cu:';
      print '<table>
                  <tr>
                      <td>Tip</td>
                      <td>'.$_SESSION['rang'].'</td>
                  </tr>
                  <tr>
                      <td>Email</td>
                      <td>'.$user_elev.'</td>
                  </tr>   
                  <tr>
                      <td>cnp</td>
                      <td>'.$_SESSION['cnp'].'</td>
                  </tr>
                 
            </table>';
        }
    else {
      include "menu.php";
      print '<div class="content">';
      print 'Esti logat cu:';
      print $_SESSION['rang'];
    }
print '</div>';
include "footer.php";
}
?>