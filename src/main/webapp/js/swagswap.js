
function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}

//From http://www.experts-exchange.com/Web/Web_Languages/Q_21243591.html
function formatStr(EL)
//the EL is the target element passed fron the invokin process in the form:
//document.getElementById('idofelement');
 {
  strbuff=EL.innerHTML;
  
  //takes the content of the element and puts it into a string
  newstr='';
  startI = 0;
  subarr=new Array(Math.floor(strbuff.length/60+1));
  //creates an array with a length that will hold all 60 character segments + the final bit.
  //in the earlier versio I was using 7 which is incorrect 60 is the required size
  for (var i=0;i<subarr.length;i++)
  {
     // this loop creates 60 character substrings an put them in array
     subarr[i]=strbuff.substr(startI,60);
     startI+=60;
  }
  for (var i=0;i<subarr.length-1;i++)
  {
     // this loop creates a new string by concatenating the elements in the array
     // with an HTML line break tag between each segment
	  //don't break author line
	 if (-1==subarr[i].indexOf("[")) {
		 newstr+=subarr[i]+'<br />';
	 } 
	 else {
		 newstr+=subarr[i];
  	}
  }
  newstr+=subarr[subarr.length-1];
    // the final segement is append outside to loop to avoid an extra linefeed
  EL.innerHTML=newstr;
   // the content of the target element is replaced with the new string
}	

