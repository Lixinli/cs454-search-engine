window.alert(5 + 2);
document.write(5 + 6);
$(document)
		.ready(
				function() {
					
					// Declare variables
					var $searchButton = $('#s');
					// add a colon to the beginning of your AppId string
					var appId = 'OoOHrxezkeLz5IadDuqHWyExHwuP+kgVmm6TcGVuc4o';
					
					// Function to get images
					function getImage() {
						// base64 encode the AppId
						var azureKey = btoa(appId);
						// get the value from the search box
						var $searchQuery = $('#searchBox').val();
						// Create the search string
						var myUrl = 'https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27image%27&$top=50&$format=json&Query=%27'
								+ $searchQuery + '%27';
						// Make post request to bing
						$.ajax({
									method : 'post',
									url : myUrl,
									// Set headers to authorize search with Bing
									headers : {
										'Authorization' : 'Basic ' + azureKey
									},
									success : function(data) {
										// Insert random image in dom
										var randomIndex = Math.floor(Math
												.random() * 50);
										var imgLink = '<img width="500px" src="'
												+ data.d.results[0].Image[randomIndex].MediaUrl
												+ '" />';
										$('#output').html(imgLink);
									},
									failure : function(err) {
										console.error(err);
									}
								});
					};
					// Trigger function when button is clicked
					$searchButton.click(function(e) {
						e.preventDefault();
						getImage();
					});
				});
