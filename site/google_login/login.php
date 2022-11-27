<?php
// Report all PHP errors
error_reporting(E_ALL);
ini_set("display_errors", 1);

//session_start();
require 'db_connection.php';
//include 'db_connection.php';

require 'google-api/vendor/autoload.php';


// Creating new google client instance
$client = new Google_Client();

// Enter your Client ID
$client->setClientId('');
// Enter your Client Secrect
$client->setClientSecret('');
// Enter the Redirect URL
//$client->setRedirectUri('https://localhost/cmv-sites/google_login/login.php');
$client->setRedirectUri('https://campus.lmvineu.ro/google_login/login.php');
//$client->setRedirectUri('https://apps.lmvineu.ro/google_login/login.php');

// Adding those scopes which we want to get (email & profile Information)
$client->addScope("email");
$client->addScope("profile");


if(isset($_GET['code'])):

    $token = $client->fetchAccessTokenWithAuthCode($_GET['code']);

    if(!isset($token["error"])){

        $client->setAccessToken($token['access_token']);

        // getting profile information
        $google_oauth = new Google_Service_Oauth2($client);
        $google_account_info = $google_oauth->userinfo->get();
    
        // Storing data into database
        $id = mysqli_real_escape_string($db_connection, $google_account_info->id);
        $full_name = mysqli_real_escape_string($db_connection, trim($google_account_info->name));
        $email = mysqli_real_escape_string($db_connection, $google_account_info->email);
        $profile_pic = mysqli_real_escape_string($db_connection, $google_account_info->picture);

        // checking admin user already exists or not
        $get_user = mysqli_query($db_connection, "SELECT `mail` FROM `admins` WHERE `mail`='$email'");
        if(mysqli_num_rows($get_user) > 0){

            $get_user = mysqli_query($db_connection, "UPDATE `admins` SET login = current_timestamp WHERE `mail`='$email'");
            
            $_SESSION['logat'] = "Adevarat";
            $_SESSION['admin'] = "Adevarat";
            $_SESSION['login_id'] = $id;
            $_SESSION['full_name'] = $full_name;
            $_SESSION['email'] = $email; 
            $_SESSION['profile_pic'] = $profile_pic;   
            //header('Location: home.php');
            //exit;

        }
        
        // checking user already exists or not
        $get_user = mysqli_query($db_connection, "SELECT `mail` FROM `diriginti` WHERE `mail`='$email'");
        if(mysqli_num_rows($get_user) > 0){

            $get_user = mysqli_query($db_connection, "UPDATE `diriginti` SET login = current_timestamp WHERE `mail`='$email'");
            
            $_SESSION['logat'] = "Adevarat";
            $_SESSION['login_id'] = $id;
            $_SESSION['full_name'] = $full_name;
            $_SESSION['email'] = $email; 
            $_SESSION['profile_pic'] = $profile_pic;   
            header('Location: home.php');
            exit;

        }elseif( $_SESSION['admin'] == "Adevarat"){
            header('Location: home.php');
            exit;
        }
        else{

            echo "Trebuie sa fi autentificat ca si diriginte cu cont de lmvineu! <br>";
            echo '<br><a class="login-btn" href="logout.php">Logout in <t id="waitSecs"></t> secunde </a>';
            print '<p id="wait"></p>';
            print' 
             <script>
             var div = document.getElementById(\'wait\');
             var div_waitSecs = document.getElementById(\'waitSecs\');
             var counter = 0;
             var waitSecs = 8;
             var refreshId = setInterval(function () {
               ++counter;
               div.innerHTML = div.innerHTML + \'&#x25cf;\'
               div_waitSecs.innerHTML=waitSecs-counter;
               if(counter == waitSecs){
                 clearInterval(refreshId);
                 top.location.href=\'logout.php\'
               }
             }, 1000);
             </script>
             ';

        }

    }
    else{
        header('Location: login.php');
        exit;
    }
    
else: 
    $Google_Login_Url = $client->createAuthUrl(); 
    //print '<a class="login-btn" href="'.$client->createAuthUrl().'">Login</a>';
    header('Location: '.$Google_Login_Url);
endif;
?>