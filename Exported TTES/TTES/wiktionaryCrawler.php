<?php

	header('Content-Type: text/html; charset=utf-8');
	$dbLink=mysql_connect("localhost","root","") or die(mysql_error());
	mysql_query("SET character_set_results=utf8", $dbLink);
	mysql_select_db("tamilwordnet",$dbLink) or die(mysql_error());
	
	function getEnglishWords($url,$tam_word)
	{
		try
		{
		global $word,$english_words;
//		$url="http://www.tamildict.com/tamilsearch.php?action=search&keyboard=&sID=ec49b9f6ec9ed6b9f9de9ff2d967d889%2F&tmode=on&word=".$word."";
		echo " The english word is ";
		$dom = new DOMDocument("1.0");
		@$dom->loadHTMLfile($url);
		//$xpath = new DomXpath($dom);

		$table = $dom->getElementById("bodyContent");
			$pos="verb";
			$debug=0;
			$english_found=0;
/*		echo "<br/>matches found are :<br/>".$table->length;
			#
		for($i=0;$i<$table->length;$i++) 
		{
			$documentLink = $table->item($i);
			if($table)
			{}
*/			
			if($table)
			{
				$tablerow = $table->getElementsByTagName("div");
				
			for($i=1;$i<$tablerow->length-1;$i++)
			{
				if(!$english_found)
				{
					$data=$tablerow->item($i);
					$str=$data->nodeValue;
					//echo $str."--------";
					$id=strpos($str,'ஆங்கிலம்');
					$id1=strpos($str,'பெயர்ச்சொல்');
					if(!$id1)
							$id1=strpos($str,'பெயர்ச்சொற்கள்');
					if(!$id1)
							$id1=strpos($str,'(பெ)');
					$count=24;
					if(!$id)
					{	
						$id=strpos($str,'மொழிபெயர்ப்புகள்');
						//echo "matching trans at ".$id."<br/>";
						$count=48;
					}
					if($id1!=false)
						$pos="noun";

					if ($id!= false) {
						 $a=substr($str,$id+$count);
						// echo "The full string is ".$a;
						 //echo "<br/><br/>";
						$a=str_replace("- ","",$a);
						$a=str_replace("ஆங்கிலம் ","",$a);
						$a=str_replace(":","",$a);
						$a=str_replace("(ஆங்)","",$a);
						$a=str_replace("1.","",$a);
						$a=str_replace("2.","",$a);

						if($debug)
						 echo "searching".$a."<br/>";
						/*$id=mb_strpos($a,"&nbsp");
						 echo " id1 is ".$id1."<br/><br/>";	 
						 $id1=strpos($a,"<br/>");
						 $id2=strpos($a,"\n");
						 //echo strlen($);
						 if($id1 && $id1<$id)
						 	$id=$id1;
						 if($id2 && $id2<$id)
						 	$id=$id2;
						 if(!$id)
						 	$id=strpos($a,',');
						 echo $id;
						 if(!$id)
						 	$id=strlen($a);*/

						 $english=substr($a,0,1);
						 $id=1;
						 $y=$a;
						 while(!ctype_alpha($english))
						 {
						 	$a=substr($a,1);
						 	if($debug)
						 		echo "Now string is ".$a."<br/> ";
						 	$english=substr($a,0,1);
						 }
						 //$a=$y;
						 $english1='nothing';
						 if($debug)
						 	echo "English ".$english."<br/><br/>";
						 while(ctype_alpha($english))
						 {
						 	 $english1=$english;
						 	 if($debug)
						 	 	echo "Now string is ".$english1	."<br/> ";
						 	 $english=substr($a,0,$id++);
						 }
						 if($debug)
						 	echo "the string ".$english." is not english <br/><br/>";

						 if($english1!='nothing')
	 				     {	
	 				     	echo "For ".$tam_word." the english we get is ".$english1." with POS (pos may be wrong) ".$pos."<br/>";
							mysql_query("insert in to testing values ('$tam_word','$pos','$english1',0)") ;
							$english_found=1;
						}
					}
					

				}

				
			/*	if($data->nodeValue=="மொழிபெயர்ப்புகள்")
				{
					//echo $tr1->item(0)->nodeValue==$word; 
					$tr1=$data->getElementsByTagName("li");
					//echo var_dump($data);
					//echo $tr1[0]->nodeValue ."<br/>";
					//array_push($english_words,$tr1->item(1)->nodeValue);
				}*/
			}
			}
		}
		catch(Exception $e)
		{
			echo "FIle not found for ".$tam_word;
		}
		
		//$english_words=array_unique($english_words);
	}

	$res=mysql_query("select label from twn") or die(mysql_error());
	while($r=mysql_fetch_array($res))
	{	
		$word=$r['label'];
		echo "The tamil word is ".$word;
		getEnglishWords("http://ta.wiktionary.org/wiki/".$word,$word); 
	}
?>