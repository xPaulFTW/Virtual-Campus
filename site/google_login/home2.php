<script language="JavaScript">
function toggle(source) {
  checkboxes = document.getElementsByName('activ[]');
  for(var i=0, n=checkboxes.length;i<n;i++) {
    checkboxes[i].checked = source.checked;
  }
}

function copy_function(myInput) {
  // Get the text field
  var copyText = document.getElementById(myInput);

  // Select the text field
  copyText.select();
  copyText.setSelectionRange(0, 99999); // For mobile devices

  // Copy the text inside the text field
  navigator.clipboard.writeText(copyText.value);
  
  // Alert the copied text
  alert("Parola copiata: " + copyText.value);
}
</script>

<?php
// Report all PHP errors
error_reporting(E_ALL);
ini_set("display_errors", 1);

//session_start();
require 'db_connection.php';
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
    //header('Location: logout.php');
    //exit;
    
print '    

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>'.$_SESSION['full_name'].'</title>
    <style>
        *,
        *::before,
        *::after {
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
        }
        body{
            font-family: \'Segoe UI\', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f7f7ff;
            padding: 10px;
            margin: 0;
        }
        ._container{
          //display: inline-block;
            min-width: 400px;
            //position: relative;
            width: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            margin: 0 auto;
            margin-right: 10px;
            margin-left: 215px;
            margin-top: 10px;
            //margin-bottom: 0px;
            border: 1px solid #cccccc;
            border-radius: 2px;
        }

        ._img{
            overflow: hidden;
            width: 100px;
            height: 100px;
            margin: 0 auto;
            border-radius: 50%;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
        }
        ._img > img{
            width: 100px;
            min-height: 100px;
        }
        ._info{
            text-align: center;
        }
        ._info h1{
            margin:10px 0;
            text-transform: capitalize;
        }
        ._info p{
            color: #555555;
        }
        ._info a{
            display: inline-block;
            background-color: #E53E3E;
            color: #fff;
            text-decoration: none;
            padding:5px 10px;
            border-radius: 2px;
            border: 1px solid rgba(0, 0, 0, 0.1);
        }

        @media screen and (max-width: 700px) {
          ._container {
            //width: 100%;
            //height: auto;
            margin-left: 210px;
            position: relative;
          }
          //.sidebar a {float: left;}
          //div.content {margin-left: 0;}
        }

        @media screen and (max-width: 400px) {
          ._container {
            margin-left: 10px;
            //text-align: center;
            float: none;
          }
        }
        
    </style>
    <style>
        body {
          margin: 0;
          font-family: "Lato", sans-serif;
        }

        .sidebar {
          margin: 0;
          padding: 0;
          width: 200px;
          background-color: #f1f1f1;
          position: fixed;
          height: 100%;
          overflow: auto;
        }

        .sidebar a {
          display: block;
          color: black;
          padding: 16px;
          text-decoration: none;
        }
        
        .sidebar a.active {
          background-color: #04AA6D;
          color: white;
        }

        .sidebar a:hover:not(.active) {
          background-color: #555;
          color: white;
        }

        div.content {
          margin-left: 200px;
          margin-right: 10px;
          width: 0 auto;
          min-width: 600px;
          //max-width: 1200px;
          height: 0 auto;
          padding: 1px 16px;
          //height: 1000px;
        }

        @media screen and (max-width: 700px) {
          .sidebar {
            width: 100%;
            height: auto;
            position: relative;
          }
          .sidebar a {float: left;}
          div.content {margin-left: 0;}
          div._container {margin-left: 10px;}
        }

        @media screen and (max-width: 400px) {
          .sidebar a {
            text-align: center;
            float: none;
          }
        }

        .scroller {
          width: 100%;
          height: 100%;
          overflow-y: scroll;
          //scrollbar-width: thin;
          scrollbar-width: auto;
        }
    </style>    

    <style>
table {
  border-collapse: collapse;
  width: 100%;
}

th, td {
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #D6EEEE;
}
</style>
</head><body>';
//<a class="login-btn" href="logout.php">Resetare parola</a><br>
print'<div class="scroller">
<div class="sidebar">
  <a class="active" href="home.php">Acasa</a>
  <a href="home.php?opt=reset">Resetare parola</a>';
  if($_SESSION['admin']=='Adevarat'){
    print '<a href="home.php?opt=reset_admin">Resetare parola admins</a>';
  }
  print '<a href="manual_site.php">Ajutor</a>
  <a href="#contact">Contact</a>
  <a href="#about">About</a>
</div>';    

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

switch($_GET['opt']){
    case 'reset':
        //nume_cls	presc_cls	clasa	grup	nivel	mail	nume_dir	presc_dir	login
        $result = mysqli_query($db_connection, "SELECT * FROM `diriginti` WHERE `mail`='".$_SESSION['email']."'");
        $org_unit='';
        if(mysqli_num_rows($result) > 0){
            
            while ($row = mysqli_fetch_row($result)) {
                //printf ("%s (%s)\n", $row[0], $row[1]);
              
            //$org_unit="'/Elevi/Liceal/Clasa a XII-a A'";
            $org_unit="'/Elevi/".$row[4]."/".$row[2]."'";
            //print $org_unit;
            }
            $optParams = array(
                'customer' => 'my_customer',
                'query' => "orgUnitPath=".$org_unit,
                'maxResults' => 100,
                'orderBy' => 'familyName',
              );
              $list_users = $service_users->users->listUsers($optParams);
              //print_r($list_users);
           

        
        print '<div class="content">';
        print $org_unit;
        print '<form action="home.php?opt=reset_pass" id="form1" method="post">
              <input type="hidden" name="org_unit" value="'. $org_unit. '">';
        print'<table border=\'1\' style="width:100%;border: 2px solid black;border-collapse: collapse;"> 
                    <tr>
                        <th><input type="checkbox" onClick="toggle(this)" />Tot<br/></th>
                        <th>Nr.crt.</th>    
                        <th>Nume</th>
                        <th>Mail</th>
                        <th>Ultima logare</th> 
                        
                    </tr>';
                    $i=0;
        if (count($list_users->getUsers()) == 0) {
            print "No users found.\n";
          } else {
            //print "Users:\n";
            //foreach ($list_users->getUsers() as $user) {
                for($i=0;$i<count($list_users->getUsers());$i++){
                    $user=$list_users->getUsers()[$i];
                    $j=$i+1;
              //printf("%s (%s) %s\n", $user->getPrimaryEmail(), $user->getName()->getFullName(),$user->getorgUnitPath());
              print '<input type="hidden" name="nume[]" value="'. $user->getName()->getFullName(). '">';
              print '<input type="hidden" name="email[]" value="'. $user->getPrimaryEmail(). '">';
              print'
                    <tr>
                        <td><input type="checkbox" class="tocheck" name="activ[]" value="'.$i.'" ></td>
                        <td>'.$j.'</td>
                        <td>'.$user->getName()->getFullName().'</td>
                        <td>'.$user->getPrimaryEmail().'</td>
                        <td>'.$user->getlastLoginTime().'</td>
                        
                    </tr>';
                    
            }
        }
        print '<tr><td>
        <input type="submit" name="formSubmit" value="Trimite" />
        </td>
        </tr>
        </table></form>';
        print '</div>';
        }else{
          print '<div class="content">Nu esti diriginte!</div>';
        }
        break;

        case 'reset_pass':
          print '<div class="content">';
            if(!isset($_POST['activ']))  {
                print "<h2>Niciun elev selectat!</h2></div>";
              } else {
               
            if(isset($_POST['org_unit'])) print $_POST['org_unit'];
            print'<table border=\'1\' style="width:100%;border: 2px solid black;border-collapse: collapse;"> 
                        <tr>
                            <th>Nr.crt.</th>    
                            <th>Nume</th>
                            <th>Mail</th> 
                            <th>Parola</th>
                            <th></th>
                            <th>Eroare</th>
                        </tr>';
                        $i=0;

                    for($i=0;$i<count($_POST['activ']);$i++){
                        $activ=$_POST['activ'][$i]; 
                        $nume=$_POST['nume'][$activ];
                        $email=$_POST['email'][$activ];
                        $pass=random_str(10);
                        $myinput="myinput".$i;
                        $j=$i+1;
                        
                        
                        $user_resource = new Google\Service\Directory\User(
                        array(
                              'password' => $pass,
                              'changePasswordAtNextLogin' => 'true',
                              )  
                        );
                      
                        
                  print'
                        <tr>
                            <td>'.$j.'</td>
                            <td>'.$nume.'</td>
                            <td>'.$email.'</td>
                            <td> <input type="text" value="'.$pass.'" id="'.$myinput.'" size="10" readonly></td>
                            <td><button onclick="copy_function(\''.$myinput.'\')">Copiaza</button></td>';
                    try {
                      $buff = $service_users->users->update($email,$user_resource);
                      print '<td>OK</td>';
                    }
                    
                    //catch exception
                    catch(Exception $e) {
                      print '<td>'.$e->getMessage().'/td>';
                    }
                  print '</tr>';
                        
                }
            
            print '
            </table>';
            print '</div>';
            }
            break;

            case 'reset_admin':
              //nume_cls	presc_cls	clasa	grup	nivel	mail	nume_dir	presc_dir	login
              $result = mysqli_query($db_connection, "SELECT * FROM `diriginti`");
              $org_unit='';
              
              if(mysqli_num_rows($result) > 0 && $_SESSION['admin']=='Adevarat'){
                print '<div class="content">';
                if(isset($_POST['org_unit2'])) {print $_POST['org_unit2']; $_SESSION['org_unit']=$_POST['org_unit2'];}
                  print'<form action="home.php?opt=reset_admin" id="form2" method="post">
                  <label for="org_unit2">Alege o clasa:</label>
                  <select id="org_unit2" name="org_unit2">
                  ';
                  while ($row = mysqli_fetch_row($result)) {
                      //printf ("%s (%s)\n", $row[0], $row[1]);
                    
                  //$org_unit="'/Elevi/Liceal/Clasa a XII-a A'";
                  $org_unit="'/Elevi/".$row[4]."/".$row[2]."'";
                  
                  if($_SESSION['org_unit']==''){
                    $_SESSION['org_unit']=$org_unit;
                  }

                  if($_SESSION['org_unit']==$org_unit){
                    print '<option value="'.$org_unit.'" selected>'.$org_unit.'</option>';//selected
                  }else{
                  print '<option value="'.$org_unit.'">'.$org_unit.'</option>';//selected
                  }
                  //print $org_unit;
                  }
                  print'</select>
                  <input type="submit" name="formSubmit2" value="Afiseaza" />
                  </form>';
                  
                  $org_unit=$_SESSION['org_unit'];
                  $optParams = array(
                      'customer' => 'my_customer',
                      'query' => "orgUnitPath=".$org_unit,
                      'maxResults' => 100,
                      'orderBy' => 'familyName',
                    );
                    $list_users = $service_users->users->listUsers($optParams);
                    //print_r($list_users);
                 
      
              
              //print '<div class="content">';
              //print $org_unit;
              print '<form action="home.php?opt=reset_pass" id="form1" method="post">
                    <input type="hidden" name="org_unit" value="'. $org_unit. '">';
              print'<table border=\'1\' style="width:100%;border: 2px solid black;border-collapse: collapse;"> 
                          <tr>
                              <th><input type="checkbox" onClick="toggle(this)" />Tot<br/></th>
                              <th>Nr.crt.</th>    
                              <th>Nume</th>
                              <th>Mail</th>
                              <th>Ultima logare</th>  
                              
                          </tr>';
                          $i=0;
              if (count($list_users->getUsers()) == 0) {
                  print "No users found.\n";
                } else {
                  //print "Users:\n";
                  //foreach ($list_users->getUsers() as $user) {
                      for($i=0;$i<count($list_users->getUsers());$i++){
                          $user=$list_users->getUsers()[$i];
                          $j=$i+1;
                    //printf("%s (%s) %s\n", $user->getPrimaryEmail(), $user->getName()->getFullName(),$user->getorgUnitPath());
                    print '<input type="hidden" name="nume[]" value="'. $user->getName()->getFullName(). '">';
                    print '<input type="hidden" name="email[]" value="'. $user->getPrimaryEmail(). '">';
                    print'
                          <tr>
                              <td><input type="checkbox" class="tocheck" name="activ[]" value="'.$i.'" ></td>
                              <td>'.$j.'</td>
                              <td>'.$user->getName()->getFullName().'</td>
                              <td>'.$user->getPrimaryEmail().'</td>
                              <td>'.$user->getlastLoginTime().'</td>  
                          </tr>';
                          
                  }
              }
              print '<tr><td>
              <input type="submit" name="formSubmit" value="Trimite" />
              </td>
              </tr>
              </table></form>';
              print '</div>';
              
              }else{
                print '<div class="content">Nu esti admin!</div>';
              }
              break;

    }//case
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
}//else


?>