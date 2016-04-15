(function(a){$.fn.tinyscrollbar=function(options){var defaults={cvsCtrlClass:'scrollbar-ctrl-cvs',leftCtrlClass:'scrollbar-leftctrl',rightCtrlClass:'scrollbar-rightctrl',contentClass:'scrollbar-content',scrollSpeed:1000,blockWidth:200,step:1,vis:5}
var options=$.extend(defaults,options);this.each(function(){var thisTabs=$(this);var thisLeftBtn=thisTabs.children("."+options.cvsCtrlClass).children("."+options.leftCtrlClass);var thisRightBtn=thisTabs.children("."+options.cvsCtrlClass).children("."+options.rightCtrlClass);var thisContent=thisTabs.children("."+options.contentClass);var itemWid;var count;var display;var ctrlMeter=options.blockWidth;var thisUL=thisContent.children('ul');display=thisContent.width();count=thisUL.children().length;if(count<=options.vis){thisRightBtn.hide();thisLeftBtn.hide();return;}
itemWid=count*ctrlMeter;thisUL.width(itemWid);var step=options.step;var currentMg=0;thisRightBtn.bind("click",function(e){var distance=itemWid-Math.abs(currentMg);if(distance>display)
{var nc=distance-display-1;if(nc<display){thisUL.animate({marginLeft:(currentMg-nc)+"px"},options.scrollSpeed);currentMg-=ctrlMeter*step;thisRightBtn.removeClass("scrollbar-rightctrl-enable");if(!thisLeftBtn.hasClass("scrollbar-leftctrl-enable"))
thisLeftBtn.addClass("scrollbar-leftctrl-enable");}
else{currentMg-=ctrlMeter*step;thisUL.animate({marginLeft:(currentMg)+"px"},options.scrollSpeed);if(!thisLeftBtn.hasClass("scrollbar-leftctrl-enable"))
thisLeftBtn.addClass("scrollbar-leftctrl-enable");}}
return false;});thisLeftBtn.bind("click",function(e){if(currentMg<0)
{currentMg+=ctrlMeter*step;thisUL.animate({marginLeft:(currentMg)+"px"},options.scrollSpeed);if(!thisRightBtn.hasClass("scrollbar-rightctrl-enable"))
thisRightBtn.addClass("scrollbar-rightctrl-enable");if(currentMg>=0)thisLeftBtn.removeClass("scrollbar-leftctrl-enable");}
else{}
return false;});thisRightBtn.addClass("scrollbar-rightctrl-enable");});};})(jQuery);