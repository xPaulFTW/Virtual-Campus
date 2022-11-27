<script>		
		// Function to GeneratePdf
		function GeneratePdf() {
      
			var element = document.getElementById('form-print');
      
      html2pdf(element);
      
		}
	</script>

	<script src=
"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
		integrity=
"sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW"
		crossorigin="anonymous">
	</script>

  <!-- Html2Pdf -->
	<script src=
"https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.8.1/html2pdf.bundle.min.js"
		integrity=
"sha512vDKWohFHe2vkVWXHp3tKvIxxXg0pJxeid5eo+UjdjME3DBFBn2F8yWOE0XmiFcFbXxrEOR1JriWEno5Ckpn15A=="
		crossorigin="anonymous">
	</script>


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
if(!isset($_POST['fname'])) $_POST['fname']='';

if(!isset($_SESSION['cnp'])) $_SESSION['cnp']='';
if(!isset($_SESSION['rang'])) $_SESSION['rang']='';
if(!isset($_SESSION['nivel'])) $_SESSION['nivel']='';
if(!isset($_SESSION['clasa'])) $_SESSION['clasa']='';
if(!isset($_SESSION['org_unit_google'])) $_SESSION['org_unit_google']='';
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
include "menu_elev.php";

print '<div class="content">';
switch($_GET['opt']){
  case 'send':
    //print $_POST['fname'];

    print '<text id ="form-print" enctype="text/plain"
		class="form-control">';
    //print $_POST['fname'];
    //adeverinta template
    print "
    <div class=WordSection1>

    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span class=SpellE><span style='font-family:\"Arial\",sans-serif;
    color:#222A35'>Şcoala</span></span><span style='font-family:\"Arial\",sans-serif;
    color:#222A35'>: Colegiul „Mihai Viteazul” </span><span lang=EN-US
    style='font-family:\"Arial\",sans-serif;color:#222A35;mso-ansi-language:EN-US'><o:p></o:p></span></p>
    
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-family:\"Arial\",sans-serif;color:#222A35'>Localitatea:
    Ineu&nbsp;<o:p></o:p></span></p>
    
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-family:\"Arial\",sans-serif;color:#222A35'>Nr. ".random_num(4)."/".date("d-m-Y")."<o:p></o:p></span></p>
    
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span class=SpellE><span style='font-family:\"Arial\",sans-serif;
    color:#222A35'>Judeţul</span></span><span style='font-family:\"Arial\",sans-serif;
    color:#222A35'> Arad</span><span style='font-size:9.0pt;font-family:\"Arial\",sans-serif;
    color:#222A35'><o:p></o:p></span></p>
      <br>
      <br>
    <p align=center style='margin:0cm;margin-bottom:.0001pt;text-align:center;
    background:white;vertical-align:baseline'><span style='font-size:20.0pt;
    font-family:\"Arial\",sans-serif;color:#222A35'>ADEVERINŢĂ<o:p></o:p></span></p>
    <br>
    <br>	
    <p style='margin:0cm;margin-bottom:.0001pt;text-indent:35.4pt;background:white;
    vertical-align:baseline'><span style='font-size:14.0pt;font-family:\"Arial\",sans-serif;
    color:#222A35'>Se <span class=SpellE>adevereşte</span> prin prezenta că elevul/eleva ".$_SESSION['full_name']."<o:p></o:p></span></p>
      
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-size:14.0pt;font-family:\"Arial\",sans-serif;
    color:#222A35'>este <span class=SpellE>inscris</span> la Colegiul </span><span
    lang=EN-US style='font-size:14.0pt;font-family:\"Arial\",sans-serif;color:#222A35;
    mso-ansi-language:EN-US'>“Mihai <span class=SpellE>Viteazul</span>” <span
    class=SpellE>Ineu</span></span><span style='font-size:14.0pt;font-family:\"Arial\",sans-serif;
    color:#222A35'> in anul <span class=SpellE>şcolar</span> 2022/2023<span
    style='mso-spacerun:yes'>  </span>in ".$_SESSION['clasa'].". Eliberăm prezenta pentru a-i
    servi la ".$_POST['fname'].".<o:p></o:p></span></p>
    <br>
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-family:\"Arial\",sans-serif;color:#222A35'>Director,<o:p></o:p></span></p>
    
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-family:\"Arial\",sans-serif;color:#222A35'>Liliana-Dana
    Neme&#537;&nbsp;
    <o:p></o:p></span></p>
      </br>
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-family:\"Arial\",sans-serif;color:#222A35'>Secretar,<o:p></o:p></span></p>
    
    <p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
    baseline'><span style='font-family:\"Arial\",sans-serif;color:#222A35'>Laura Onoi
    
      </br>
    <p class=MsoNormal><o:p>&nbsp;</o:p></p>
    
    </div>";
   
    //adeverinta template gata
    
    print '</text>';
    print '<br><br><input type="button" class="btn btn-primary"
		onclick="GeneratePdf();" value="GeneratePdf">';
    
    break;
    default:
print'
<form action="/google_login/adv.php?opt=send" method="post">
  <label for="fname">Motivul adeverintei</label><br>
  <input type="text" id="fname" name="fname" placeholder="Introdu pentru ce iti trebuie"><br>
  <br>
  <input type="submit" value="Trimite">
</form> 
';
}


//print '<table><tr><td>aa</td><td>bb</td></tr>  <tr><td>cc</td><td>dd</td></tr></table>';
print '</div>';
include "footer.php";
}
?>