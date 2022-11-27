<?php
// Initialize the session.
// If you are using session_name("something"), don't forget it now!
error_reporting(E_ALL);
ini_set("display_errors", 1);
session_start();
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

$_SESSION['logat']='';
$_SESSION['admin']='';
$_SESSION['org_unit']='';
$_SESSION['login_id']='';
$_SESSION['full_name']='';
$_SESSION['email']='';
$_SESSION['profile_pic']='';

$_SESSION['cnp']='';
$_SESSION['rang']='';
$_SESSION['nivel']='';
$_SESSION['clasa']='';
$_SESSION['org_unit_google']='';

// Unset all of the session variables.
$_SESSION = array();

// If it's desired to kill the session, also delete the session cookie.
// Note: This will destroy the session, and not just the session data!
if (ini_get("session.use_cookies")) {
    $params = session_get_cookie_params();
    setcookie(session_name(), '', time() - 42000,
        $params["path"], $params["domain"],
        $params["secure"], $params["httponly"]
    );
}

// Finally, destroy the session.
session_destroy();
//unset($_SESSION);

//header("Location: login.php");
//header("Location: https://mail.google.com/");
//exit;
header('Location:https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=https://azure.lmvineu.ro/cmv-sites/google_login/login.php');
?>