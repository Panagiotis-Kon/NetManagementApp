<!--A Design by W3layouts
Author: W3layout
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Hosting Store A Hosting Category Flat Bootstarp Resposive Website Template | Login :: w3layouts</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Hosting Store Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>

<spring:url value="/resources/theme/css/bootstrap.css" var="bootCSS" />	
<link href="${bootCSS}" rel='stylesheet' type='text/css' />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<spring:url value="/resources/theme/js/jquery-1.11.1.min.js" var="minJS" />
<script src="${minJS}"></script>
<!-- Custom Theme files -->
<spring:url value="/resources/theme/css/style.css" var="styleCSS" />	
<link href="${styleCSS}" rel='stylesheet' type='text/css' />
<!-- Custom Theme files -->
<!-- webfonts -->
<link href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700' rel='stylesheet' type='text/css'>
<!-- webfonts -->
<!-- dropdown -->
<spring:url value="/resources/theme/js/jquery.easydropdown.js" var="easydropJS" />
<script src="${easydropJS}"></script>
<spring:url value="/resources/theme/css/nav.css" var="navCSS" />
<link href="${navCSS}" rel="stylesheet" type="text/css" media="all"/>
<!-- start-smoth-scrolling -->
<spring:url value="/resources/theme/js/move-top.js" var="movetopJS" />
<script type="text/javascript" src="${movetopJS}"></script>
<spring:url value="/resources/theme/js/easing.js" var="easingJS" />
		<script type="text/javascript" src="${easingJS}"></script>
		<script type="text/javascript">
			jQuery(document).ready(function($) {
				$(".scroll").click(function(event){		
					event.preventDefault();
					$('html,body').animate({scrollTop:$(this.hash).offset().top},1000);
				});
			});
		</script>
<!-- end-smoth-scrolling -->
</head>
<!-- Header Starts Here -->
<div class="banner inner-banner" >
		<div class="header">
	<div class="container">
		
		
	   <span class="menu"> Menu</span>
			
		<div class="banner-top">
								<ul class="nav banner-nav">                                     
										<li><a class="active" href="index.html">Home</a></li>
										<li class="dropdown1"><a class="down-scroll" href="">Data Processing</a>
											<ul class="dropdown2">
												<li><a href="sharedhosting.html">Shared hosting</a></li>
												<li><a href="vps.html">Cloud VPS</a></li>
												<li><a href="dedicated-servers.html">Dedicated servers</a></li>
											</ul>
										</li>     
										<li class="dropdown1"><a href="domain.html">Data Visualization</a>
										</li>  
										
										<li class="dropdown1"><a class="down-scroll" href="">Data Analysis</a>
											<ul class="dropdown2">
												<li><a href="blog.html">Blog posts</a></li>
												<li><a href="single.html">Single post</a></li>
											</ul>
										</li>
										<li class="dropdown1"><a href="contact.html">Bar Diagrams</a></li> 						
										<li class="dropdown1"><a href="contact.html">Login</a></li>  
								</ul>
								<script>
									$("span.menu").click(function(){
										$(" ul.nav").slideToggle("slow" , function(){
										});
									});
								</script>
							</div>
		<div class="clearfix"> </div>

	</div>
	</div>
<!-- page heading -->
	<div id="breadcrumb_wrapper">
		<div class="container">
			
			<h3>Login</h3>
			<h6>add a short description here</h6>

			<div class="clearfix"></div>
		</div>
	</div>
</div>
<div class="clearfix"> </div>
<!-- Header Ends Here -->	
	<div class="login-content">
		<div class="container">
		<div class="login-page">
			   <div class="account_grid">
			   <div class="col-md-6 login-left wow fadeInLeft" data-wow-delay="0.4s">
			  	 <h3>NEW CUSTOMERS</h3>
				 <p>By creating an account with our store, you will be able to move through the checkout process faster, store multiple shipping addresses, view and track your orders in your account and more.</p>
				 <a class="acount-btn" href="register.html">Create an Account</a>
			   </div>
			   <div class="col-md-6 login-right wow fadeInRight" data-wow-delay="0.4s">
			  	<h3>REGISTERED CUSTOMERS</h3>
				<p>If you have an account with us, please log in.</p>
				<form>
				  <div>
					<span>Email Address<label>*</label></span>
					<input type="text"> 
				  </div>
				  <div>
					<span>Password<label>*</label></span>
					<input type="text"> 
				  </div>
				  <a class="forgot" href="#">Forgot Your Password?</a>
				  <input type="submit" value="Login">
			    </form>
			   </div>	
			   <div class="clearfix"> </div>
			 </div>
		   </div>
		</div>
	</div>
	
<!-- #Testimonials -->
		<div id="testimonialWrapper">
			<div class="container">
				<div class="row">
					<div class="col-md-2 col-md-offset-1">
						<div id="testimonialPicture"></div>
					</div>
					<div class="col-md-7 col-md-offset-1">
						<h2>What our clients are saying...</h2>
						<h3>“MyVPS is awesome! Reliable, secure hosting, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel pharetra diam. Aenean convallis nibh facilisis risus and clients. 9/12, 5 Stars all around lorem ipsum dolto sit amet!”</h3>
						<a href="#">Lorem ipsum - www.google.com</a>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
		<!-- /#Testimonials -->
<!-- #Footer -->
		<footer>
			<div class="container">
				<div class="row">
					<div class="col-md-6">
						<span class="title">Latest Tweets</span>
						<p>Lorem ipsum dolor sit amet, <span class="greenText">#tweetTag</span> consectetur adipiscing elit. Ut vel pharetra diam. Aenean convallis nibh facilisis risus convallis, non ornare tellus vehicula.<span class="tweetTime">10 Minutes Ago</span></p>
						<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel pharetra diam. Aenean convallis nibh facilisis risus convallis, non ornare tellus vehicula.<span class="tweetTime">1 Hour Ago</span></p>
					</div>
					<div class="col-md-2 col-xs-6 col-sm-4">
						<ul class="footerMenu">
							<li class="title">What we do</li>
							<li><a href="#">Plans</a></li>
							<li><a href="#">Overview</a></li>
							<li><a href="#">Features Tour</a></li>
							<li><a href="#">CloudFlare's Network</a></li>
							<li><a href="#">CloudFlare Apps</a></li>
						</ul>
					</div>
					<div class="col-md-2 col-xs-6 col-sm-4">
						<ul class="footerMenu">
							<li class="title">Services</li>
							<li><a href="#">Virtual Private Servers</a></li>
							<li><a href="#">Dedicated Servers</a></li>
							<li><a href="#">cloud vps</a></li>
							<li><a href="#">Server Management</a></li>
							<li><a href="#">Script Installation</a></li>
						</ul>
					</div>
					<div class="col-md-2 col-xs-6 col-sm-4">
						<ul class="footerAddress">
							<li class="title">Contact Us</li>
							
							<li>p. Palace, Bangalore Palace Road, India, LP4 2P8</li>
							<li><strong>Phone:</strong></li>
							<li>080 1249 4654</li>
						</ul>
					</div>
				</div>
				<hr>
			</div>			
			<div class="copyrights">
				<div class="container">
					<p>Copyrights &copy; 2015 Hosting_Store All rights reserved | Design by <a href="http://w3layouts.com/">W3layouts</a></p>
				</div>
			</div>
		</footer>
		<!-- /#Footer -->
		<!-- here stars scrolling icon -->
	<script type="text/javascript">
									$(document).ready(function() {
										/*
										var defaults = {
								  			containerID: 'toTop', // fading element id
											containerHoverID: 'toTopHover', // fading element hover id
											scrollSpeed: 1200,
											easingType: 'linear' 
								 		};
										*/
										
										$().UItoTop({ easingType: 'easeOutQuart' });
										
									});
	</script>
	<!-- //here ends scrolling icon -->
</body>
</html>