(function($){$.fn.accordion=function(options){var defaults={}
var options=$.extend(defaults,options);return this.each(function(){var $this=$(this);$block=$this.children('.ui-accordion-block');$block.each(function(){var $panel=$(this);var $btnCol=createBtn('cancel',$panel.attr('id'));$panel.children('.ui-ribbon').append($btnCol);$panel.children(".ui-panel").hover(function(){$(this).addClass('bg-grey-f8');$(this).removeClass('bg-white');},function(){$(this).addClass('bg-white');$(this).removeClass('bg-grey-f8');});});});};$.fn.accordion.focusErrow=function(targetId){$('#'+targetId).children('.ui-panel').css('background-color','#ffecdf');};$.fn.accordion.focuswIn=function(targetId){$('#'+targetId).children('.ui-panel').css('background-color','#f8f8f8');};function createBtn(type,targetId){var $btn=$("<a/>").addClass('ui-ribbon-btn').addClass('btnCollapse');switch(type){case'cancel':$btn.addClass('btnCollapse').html('[&nbsp;关闭&nbsp;]').attr('data-target',targetId).click(function(e){$('#'+$(this).attr('data-target')).children('.ui-panel').empty();return false;});break;case'edit':$btn.addClass('btnOpen').html('[&nbsp;编辑&nbsp;]').attr('data-target',targetId).click(function(e){$('#'+$(this).attr('data-target')).children('.ui-panel').empty();return false;});break;default:alert('按钮类型错误！');break;}
return $btn;}})(jQuery);