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
<title>Hosting Store A Hosting Category Flat Bootstarp Resposive Website Template | Home :: w3layouts</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Hosting Store Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>


<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> 
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

<script type="text/javascript">
function doLogin()
{	
	// alert("Hey, who clicked me?");
	 $.ajax({
			type:'GET',
			url: "/nmapp/login",
			success: function(data){
		        alert("all good"); 
		    },
		    error: function() { 
		       alert("Error Occurred"); 
		    }
			
		});
		
}
</script>
<!-- end-smoth-scrolling -->
</head>
<!-- Header Starts Here -->
<div class="banner" >
		<div class="header">
	<div class="container">
		<div class="logo">
			
		</div>
		<!--  div class="search-bar">
			<input type="text"  value="Search" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Search';}">
			<input type="submit" value="" />
	   </div-->
		<span class="menu"> Menu</span>
			
		<div class="banner-top">
								<ul class="nav banner-nav">                                     
										<li><a class="active" href="index.jsp">Home</a></li>
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
										<li class="dropdown1"><a href="login.jsp" >Bar Diagrams</a></li> 						
										<li class="dropdown1"><a href="javascript:doLogin();">Login</a></li>  
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
			<div class="banner-info">
			<h1>Please login to our page</h1>
			<p>if you don't have an account please register</p>
		</div>
</div>
<div class="clearfix"> </div>
<!-- Header Ends Here -->	
<div class="best">
	<div class="container">
		<article>
			
			<figure class="float-left"><img src="resources/theme/images/470x240.jpg" alt="Placeholder"></figure>
			
			<p>Please login to our page</p>
		</article>	
	</div>	
</div>

<div class="plans">
		<div class="container">

			<!-- pricing table -->
			<h1 class="heading">Most Popular Plans</h1>

			<div class="col-md-4 one_third pricing">
				<div class="pricing_top">
					<h6>Starter Plan</h6>
					<p><sup>$</sup>8.95</p>
				</div>
				<div class="pricing_middle">
					<ul>
						<li>10GB Disk Space</li>
						<li>100GB Bandwidth</li>
						<li>Free DDoS Protection</li>
						<li>Free Daily Backups</li>
						<li>Managed Hosting</li>
					</ul>
				</div>
				<div class="pricing_bottom">
					<a href="#">Order</a>
				</div>
			</div>

			<div class="col-md-4 one_third pricing">
				<div class="pricing_top">
					<h6>Advanced Plan</h6>
					<p><sup>$</sup>24.95</p>
				</div>
				<div class="pricing_middle">
					<ul>
						<li>10GB Disk Space</li>
						<li>100GB Bandwidth</li>
						<li>Free DDoS Protection</li>
						<li>Free Daily Backups</li>
						<li>Managed Hosting</li>
					</ul>
				</div>
				<div class="pricing_bottom">
					<a href="#">Order</a>
				</div>
			</div>

			<div class="col-md-4 one_third last pricing">
				<div class="pricing_top">
					<h6>Business Plan</h6>
					<p><sup>$</sup>39.95</p>
				</div>
				<div class="pricing_middle">
					<ul>
						<li>10GB Disk Space</li>
						<li>100GB Bandwidth</li>
						<li>Free DDoS Protection</li>
						<li>Free Daily Backups</li>
						<li>Managed Hosting</li>
					</ul>
				</div>
				<div class="pricing_bottom">
					<a href="#">Order</a>
				</div>
			</div>

			<div class="clearfix"></div>
			
		</div>
	</div>
	
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
							<li><strong>Address:</strong></li>
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