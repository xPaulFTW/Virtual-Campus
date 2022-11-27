<?php
$host = $_SERVER[HTTP_HOST];
$file=$_SERVER[REQUEST_URI];

$link_android='https://www.lastbreach.com/blog/importing-private-ca-certificates-in-android';
$link_firefox='https://docs.titanhq.com/en/3834-importing-ssl-certificate-in-mozilla-firefox.html';
//print "host=".$host." file=".$file;
$actual_link = "http://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
$actual_link_secure = "https://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
$ext = pathinfo(
    parse_url($actual_link_secure, PHP_URL_PATH), 
    PATHINFO_EXTENSION
);
if($ext=='php')
    {
    $folder=dirname($actual_link);
    $folder=$folder.'/';
    $folder_secure=dirname($actual_link_secure);
    $folder_secure=$folder_secure.'/';
    }else{
    $folder=$actual_link;
    $folder_secure=$actual_link_secure;
    }
//print '<br>'.$ext;
//print '<br>'.$actual_link_secure;
//print '<br>'.$folder_secure;

if (!isset($_SERVER['HTTPS'])){
    print '
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Logare_lmv</title>
    </head>
    <body>
    <p><a class="login-btn" href="'.$folder_secure.'index.php">Acasa_sec</a></p>
    <p>Pentru a va loga securizat accesati link-ul:
        <a class="login-btn" href="'.$folder_secure.'login.php">Login</a>
    </p>
    <p>Pentru a putea accesa securizat site-ul trebuie instalat urmatorul certificat: <a class="login-btn" href="cmvCA.pem" download>Certificat CA</a></p>
    
    <p>Un tutorial pentru instalarea certificatului CA in Firefox: <a class="login-btn" href="'.$link_firefox.'">Tutorial Firefox Certificat CA</a></p>
    <p>Un tutorial pentru instalarea certificatului CA pe Android: <a class="login-btn" href="'.$link_android.'">Tutorial Android Certificat CA</a></p>
        
    </body>
    </html>
    ';  
} else{
print '
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Logare_lmv</title>
</head>
<body>
<H2>Esti pe varianta securizata a site-ului destinata dirigintilor!</H2>
<p>Pentru a va loga securizat accesati link-ul:
<a class="login-btn" href="./login.php">Login</a>
</p>
<p>Daca nu ai instalat certificatul ai link-ul aici: <a class="login-btn" href="cmvCA.pem" download>Certificat CA</a></p>

<p>Un tutorial pentru instalarea certificatului CA in Firefox: <a class="login-btn" href="'.$link_firefox.'">Tutorial Firefox Certificat CA</a></p>
<p>Un tutorial pentru instalarea certificatului CA pe Android: <a class="login-btn" href="'.$link_android.'">Tutorial Android Certificat CA</a></p>
</body>
</html>
';
}
?>