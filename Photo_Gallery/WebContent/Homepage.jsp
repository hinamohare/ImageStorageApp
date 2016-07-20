<!DOCTYPE html>
<html>
    <head>
	<style type="text/css">
		<!--
		body { 
				background-image: url(img/sky.jpg);
				-webkit-background-size: cover;
				-moz-background-size: cover;
				-o-background-size: cover;
				background-size: cover;
				background-repeat: no-repeat; 
			}
		
			-->
	</style>
	<title style="color:skyblue;">Photo Gallery</title>
    </head>
    <body >
      
		<div style="background-color:black; color:white; padding:0.05px;">	
		<h1 style="fond-family:courier; text-align:center;">Photo Gallery</h1>
		</div>
	
		<p style="text-align:right;">
		
			<% //retrive the session userName to represent the logged in user
	   		 HttpSession sessionvalue = request.getSession(false); 	   
	  		 String userName = (String) sessionvalue.getAttribute("userName"); 
	   		 out.print(userName);
	   		 %>
	   		 <br>
			
			<a href="Login" >logout</a>
		</p>
	
		<!-- This is a form to upload photos -->
		<div style=" line-height:30px; height:300px; width:300px; float:left; padding:5px;">
		<p> 	
			<legend><strong><i>Select image to upload</i></strong></legend>
        	
        	<form name="uploadimage" action="Upload" method="post" enctype="multipart/form-data">
				
				 	<input type="file" name="photo" accept="image/*"> <br>
				
					<i>Photo Name</i>: <input type="text" name="photoname">
				
					<input type="submit" name="button" value="upload" >
			</form>
		</p>
		</div>
		


	<!-- Display the uploaded photos in a tabular form -->

	<%@ page import="executors.Photos"%>
	<%@ page import="executors.PhotoInfo"%>
	<%@ page import="java.util.ArrayList"%>

	<%
		HttpSession contextSession = request.getSession(false);
		int userId = (int) contextSession.getAttribute("userId");
	//	ArrayList photos = Photos.GetPhotoIDsByUserId(userId);
	
	ArrayList<PhotoInfo> photos =  Photos.GetPhotoIDsByUserId(userId);
		int PhotoCount=photos.size();
	%>
	
	<h3>
		<strong><i>This is your Gallery</i></strong>
	</h3>
	<%
		if (PhotoCount > 0) {
	%>
	
	<i>You have photos: <%=PhotoCount %></i> 
	

	<table border="0" align="center" width="70%">
		
		<% 
			for (PhotoInfo photoObj : photos) {
				String ahreforiginal = String.format("/Photo_Gallary/DisplayPhoto?photoId=%1$d&type=original", photoObj.photoID)	;	
				String ahrefthumbnail = String.format("/Photo_Gallary/DisplayPhoto?photoId=%1$d&type=thumbnail", photoObj.photoID)	;	
				String ahrefdelete = String.format("/Photo_Gallary/DeletePhoto?photoId=%1$d", photoObj.photoID)	;
		%>
	 	<tr><td><a href='<%=ahreforiginal%>'><img width=100 height=100 src='<%=ahrefthumbnail%>'/></a> <br><%=photoObj.photoName %>
			<a href='<%=ahrefdelete %>'><img width="30" height="30" src="/Photo_Gallary/img/deleteIcon.png"/></a>
		</td></tr> 
			
		<%}%> 	</table>
	<%} else {%>
	<h4>
		<i>You do not have any photo in your gallery</i>
	</h4>
	<%}	%>
	

</body>		
    </body>
</html>