var selectedOAuthFileCount, totalOAuthUploadedValue, fileOAuthCount, filesOAuthUploaded;

function onUploadOAuthComplete(e) {
	totalOAuthUploadedValue += document.getElementById('files').files[filesOAuthUploaded].size;
	filesOAuthUploaded++;
	if (filesOAuthUploaded < fileOAuthCount) {
		uploadOAuthNext();
	} else {
		var load_bar= document.getElementById('bar');
		load_bar.style.width = '100%';
		load_bar.innerHTML = '100% complete';
		swal("Success!", "Finished uploading file(s)", "success");
	}
}

function onOAuthFileSelection(e) {
	files = e.target.files; // FileList object
	var output = [];
	fileOAuthCount = files.length;
	selectedOAuthFileCount = 0;
	for (var i = 0; i < fileOAuthCount; i++) {
		var file = files[i];
		output.push(file.name, ' (', file.size, ' bytes, ', file.lastModifiedDate
				.toLocaleDateString(), ')');
		output.push('<br/>');
		selectedOAuthFileCount += file.size;
	}
	document.getElementById('selectedFiles').innerHTML = output.join('');

}

function onUploadOAuthProgress(e) {
	if (e.lengthComputable) {
		var percentComplete = parseInt((e.loaded + totalOAuthUploadedValue) * 100 / selectedOAuthFileCount);
		var load_bar = document.getElementById('bar');
		load_bar.style.width = percentComplete + '%';
		load_bar.innerHTML = percentComplete + ' % Completed';
	} else {
		console.err("Unable to compute length");
	}
}

function onUploadOAuthFailed(e) {
	swal("Error!", "Error uploading file(s)", "danger");
}

function uploadOAuthNext() {
	var xhrequest = new XMLHttpRequest();
	var formDate = new FormData();
	var fileOAuth = document.getElementById('files').files[filesOAuthUploaded];
	formDate.append("multipartFile", fileOAuth);
	xhrequest.upload.addEventListener("progress", onUploadOAuthProgress, false);
	xhrequest.addEventListener("load", onUploadOAuthComplete, false);
	xhrequest.addEventListener("error", onUploadOAuthFailed, false);
	xhrequest.open("POST", "/upload");
	xhrequest.send(formDate);
}

function startOAuthUpload() {
	if (document.getElementById('files').files.length <= 0) {
		swal("Cannot Upload!", "Please select file (s) to upload", "warning");
	} else {
		totalOAuthUploadedValue = filesOAuthUploaded = 0;
		uploadOAuthNext();
	}
}

function resetOAuthScreen() {
	document.getElementById('bar').style.width = '0%';
	document.getElementById('bar').innerText = '';
	document.getElementById("selectedFiles").innerHTML = '';
	document.getElementById("imageForm").reset();
}

window.onload = function() {
	document.getElementById('files').addEventListener('change', onOAuthFileSelection, false);
	document.getElementById('uploadButton').addEventListener('click', startOAuthUpload, false);
	document.getElementById('resetButton').addEventListener('click', resetOAuthScreen, false);
}
