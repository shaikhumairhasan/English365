/*

function displayImage() {
	var input = document.getElementById("customFile2");
	var image = document.getElementById("profileImage");

	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			image.src = e.target.result;
		};

		reader.readAsDataURL(input.files[0]);
	}
}


function loadContent(url) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("content").innerHTML = xhr.responseText;
        }
    };
    xhr.send();
}

// Function to parse the URL and determine the section number
function loadSectionFromURL() {
   // Get the URL parameters
   const urlParams = new URLSearchParams(window.location.search);
   const sectionNumber = urlParams.get('section');

   // Define the mapping of section numbers to URLs
   const sectionMapping = {
	  '1': '/dailyVocab',
      '2': '/imageTranslate',
      '3': '/tts',
      //'4': '/dictionary',
      // Add more mappings as needed
       };

// Load the corresponding section if a valid section number is provided
   if (sectionNumber && sectionMapping[sectionNumber]) {
   loadContent(sectionMapping[sectionNumber]);
   }
}



function checkPasswordMatch() {
	var password = document.getElementById("password").value;
	var confirmPassword = document.getElementById("confirmPassword").value;

	if (password !== confirmPassword) {
		alert("Password and Confirm Password do not match.");
		return false;
	}
	return true;
}

document.getElementById("registrationForm").addEventListener("submit", function (event) {
	if (!checkPasswordMatch()) {
		event.preventDefault();
	}
});

*/