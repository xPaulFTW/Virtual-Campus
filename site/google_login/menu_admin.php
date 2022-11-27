<?php

error_reporting(E_ALL);
ini_set("display_errors", 1);

//session_start();
require_once 'db_connection.php';
//mysqli_report(MYSQLI_REPORT_ALL);
//mysqli_report(MYSQLI_REPORT_ERROR);


print '<div class="sidebar">
  <a class="active" href="home.php">Acasa</a>
  <a href="reset_pass_class.php?opt=reset">Resetare parola</a>
  <a href="reset_pass_class.php?opt=reset_admin">Resetare parola admin</a>
  <a href="bilet.php">Bilet de voie</a>
  <a href="adv.php">Adeverinta</a>
  <a href="pdf.php">Adeverinta2</a>
  <a href="manual_site.php">Ajutor</a>
  <a href="#contact">Contact</a>
  <a href="#about">About</a>
  <a href="../index.html">Campus CMV</a>
</div>';

?>