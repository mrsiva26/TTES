<?php

	header('Content-Type: text/html; charset=utf-8');
	mysql_connect("localhost","root","");
	mysql_select_db("tamilwordnet");
	
	function getEnglishWords($url,$tam_word)
	{
		global $word,$english_words;
//		$url="http://www.tamildict.com/tamilsearch.php?action=search&keyboard=&sID=ec49b9f6ec9ed6b9f9de9ff2d967d889%2F&tmode=on&word=".$word."";
		echo " The english word is ";
		$dom = new DOMDocument("1.0");
		@$dom->loadHTMLfile($url);
		//$xpath = new DomXpath($dom);

		$table = $dom->getElementById("bodyContent");
			$pos="verb";
/*		echo "<br/>matches found are :<br/>".$table->length;
			#
		for($i=0;$i<$table->length;$i++) 
		{
			$documentLink = $table->item($i);
*/			$tablerow = $table->getElementsByTagName("div");
				
			for($i=1;$i<$tablerow->length-1;$i++)
			{
				$data=$tablerow->item($i);
				$str=$data->nodeValue;
				//echo $str."--------";
				$id=strpos($str,'ஆங்கிலம்');
				$id1=strpos($str,'பெயர்ச்சொல்');
				if($id1!=false)
					$pos="noun";

				if ($id!= false) {
					 $a=substr($str,$id+24);
					 $a=str_replace("- ","",$a);
					 //echo "searching".$a;
					 $id=strpos($a,',');
					 //echo strlen($);
					 if(!$id)
					 	$id=strpos($a,' ');
					 echo $id;
					 if(!$id)
					 	$id=strlen($a);
					 $english=substr($a,0,$id);
 				     echo "For".$tam_word."===".$english."=== with POS".$pos."==";
				
				}
				mysql_query("insert in to testing values ('$tam_word','$pos','$english',0)") ;
			/*	if($data->nodeValue=="மொழிபெயர்ப்புகள்")
				{
					//echo $tr1->item(0)->nodeValue==$word; 
					$tr1=$data->getElementsByTagName("li");
					//echo var_dump($data);
					//echo $tr1[0]->nodeValue ."<br/>";
					//array_push($english_words,$tr1->item(1)->nodeValue);
				}*/
			}
		
		
		//$english_words=array_unique($english_words);
	}

	$res=mysql_query("select label from twn");
	while($r=mysql_fetch_array($res))
	{	
		$word=$r['label'];
		getEnglishWords("http://ta.wiktionary.org/wiki/".$word,$word); 
	}
?>