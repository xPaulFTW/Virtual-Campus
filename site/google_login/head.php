<?php
    
print '    

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>CMV_apps</title>
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
</head><body>

<div class="scroller">
';
?>