<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content=
	"width=device-width,initial-scale=1.0">

	<!-- CSS only -->
	<link href=
"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css"
		integrity=
"sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1"
		crossorigin="anonymous" rel="stylesheet">

	<!-- Html2Pdf -->
	<script src=
"https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.8.1/html2pdf.bundle.min.js"
		integrity=
"sha512vDKWohFHe2vkVWXHp3tKvIxxXg0pJxeid5eo+UjdjME3DBFBn2F8yWOE0XmiFcFbXxrEOR1JriWEno5Ckpn15A=="
		crossorigin="anonymous">
	</script>

	<style>
		.heading{
		text-align: center;
		color: #2F8D46;
		}
	</style>
</head>

<body>
	
    <!-- <h2 class="heading">
	Adeverinte elevi
	</h2> -->

	<!-- Form encrypted as text -->
	<form id ="form-print" enctype="text/plain"
		class="form-control">

        <div class=WordSection1>

<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span class=SpellE><span style='font-family:"Arial",sans-serif;
color:#222A35'>Şcoala</span></span><span style='font-family:"Arial",sans-serif;
color:#222A35'>: Colegiul „Mihai Viteazul” </span><span lang=EN-US
style='font-family:"Arial",sans-serif;color:#222A35;mso-ansi-language:EN-US'><o:p></o:p></span></p>

<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-family:"Arial",sans-serif;color:#222A35'>Localitatea:
Ineu&nbsp;<o:p></o:p></span></p>

<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-family:"Arial",sans-serif;color:#222A35'>Nr. ………./
……………<o:p></o:p></span></p>

<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span class=SpellE><span style='font-family:"Arial",sans-serif;
color:#222A35'>Judeţul</span></span><span style='font-family:"Arial",sans-serif;
color:#222A35'> Arad</span><span style='font-size:9.0pt;font-family:"Arial",sans-serif;
color:#222A35'><o:p></o:p></span></p>
	
<p align=center style='margin:0cm;margin-bottom:.0001pt;text-align:center;
background:white;vertical-align:baseline'><span style='font-size:20.0pt;
font-family:"Arial",sans-serif;color:#222A35'>ADEVERINŢĂ<o:p></o:p></span></p>
<br>	
<p style='margin:0cm;margin-bottom:.0001pt;text-indent:35.4pt;background:white;
vertical-align:baseline'><span style='font-size:14.0pt;font-family:"Arial",sans-serif;
color:#222A35'>Se <span class=SpellE>adevereşte</span> prin prezenta că elevul
(eleva) Ion<o:p></o:p></span></p>
	
<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-size:14.0pt;font-family:"Arial",sans-serif;
color:#222A35'>este <span class=SpellE>inscris</span> la Colegiul </span><span
lang=EN-US style='font-size:14.0pt;font-family:"Arial",sans-serif;color:#222A35;
mso-ansi-language:EN-US'>“Mihai <span class=SpellE>Viteazul</span>” <span
class=SpellE>Ineu</span></span><span style='font-size:14.0pt;font-family:"Arial",sans-serif;
color:#222A35'> in anul <span class=SpellE>şcolar</span> 2022/2023<span
style='mso-spacerun:yes'>  </span>in clasa a IX-a. Eliberăm prezenta pentru a-i
servi la <input class="form-control" type="text"
		id="name" name="Name" placeholder="Introdu pentru ce iti trebuie">.<o:p></o:p></span></p>
<br>
<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-family:"Arial",sans-serif;color:#222A35'>Director,<o:p></o:p></span></p>

<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-family:"Arial",sans-serif;color:#222A35'>Liliana-Dana
Neme&#537;&nbsp;
<o:p></o:p></span></p>
	</br>
<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-family:"Arial",sans-serif;color:#222A35'>Secretar,<o:p></o:p></span></p>

<p style='margin:0cm;margin-bottom:.0001pt;background:white;vertical-align:
baseline'><span style='font-family:"Arial",sans-serif;color:#222A35'>Laura Onoi

	</br>
<p class=MsoNormal><o:p>&nbsp;</o:p></p>

</div>
				
		
		<input type="button" class="btn btn-primary"
		onclick="GeneratePdf();" value="GeneratePdf">
	</form>
				
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
</body>

</html>
