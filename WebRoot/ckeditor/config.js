/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
var pathName = window.document.location.pathname;
    //鑾峰彇甯�/"鐨勯」鐩悕锛屽锛�uimcardprj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    CKEDITOR_MEDIA_PREFIX=projectName;  
    config.filebrowserImageUploadUrl = projectName+'/actions/ckeditorUpload'; //鍥哄畾璺緞
   config.baseHref="http://127.0.0.1:8080/TestCK/"
	config.filebrowserUploadUrl="actions/ckeditorUpload";
   config.image_previewText = ' ';
	config.extraPlugins += (config.extraPlugins ?',lineheight' :'lineheight');
	 /*
	   config.toolbar = "Full";
	 config.toolbar_Full = [
	 ['Source','Screen'],
	 ['Bold','Italic','Underline','Strike'],
	 ['NumberedList','BulletedList','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	 	 ['Image','Table','HorizontalRule'],
	 	 ['Format','Font','FontSize','lineheight'],
	['TextColor','BGColor']
	 ];
	 */
	config.toolbar = "Full";
	 config.toolbar_Full = [
	 ['Source','Screen'],
	 ['Bold'],
	 ['NumberedList','-','JustifyLeft','JustifyCenter','JustifyRight'],
	 	 ['Image','Table','FontSize'],
	['TextColor','BGColor']
	 ];
	 config.width = "480px";
	
	 
	 config.height = "480px";
	 config.resize_enabled = false;
	
};
