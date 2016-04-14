;
(function ($) {
	$.fn.slide = function (options) {
		$.fn.slide.defaults = {
			type : "slide",
			effect : "fade",
			autoPlay : false,
			delayTime : 500,
			interTime : 10000,
			triggerTime : 150,
			defaultIndex : 0,
			titCell : ".hd li",
			mainCell : ".bd",
			targetCell : null,
			trigger : "mouseover",
			scroll : 1,
			vis : 1,
			titOnClassName : "on",
			autoPage : false,
			prevCell : ".prev",
			nextCell : ".next",
			pageStateCell : ".pageState",
			opp : false,
			pnLoop : true,
			easing : "swing",
			startFun : null,
			endFun : null,
			switchLoad : null,
			cloneDeep : true,
			stepEffect : false,
			playStateCell : ".playState",
			mouseOverStop : true,
			defaultPlay : true,
			returnDefault : false,
			scrollStop : false,
			hookEle : "",
			conOnClassName : "conOn"
		};
		return this.each(function () {
			var opts = $.extend({}, $.fn.slide.defaults, options),
			slider = $(this),
			hookEle = $(opts.hookEle),
			_initX = 0,
			_finishX = 0,
			_startX = 0,
			_startY = 0,
			effect = opts.effect,
			prevBtn = $(opts.prevCell, slider),
			nextBtn = $(opts.nextCell, slider),
			pageState = $(opts.pageStateCell, slider),
			playState = $(opts.playStateCell, slider),
			navObj = $(opts.titCell, slider),
			navObjSize = navObj.size(),
			conBox = $(opts.mainCell, slider),
			conBoxSize = conBox.children().size(),
			sLoad = opts.switchLoad,
			tarObj = $(opts.targetCell, slider),
			index = parseInt(opts.defaultIndex),
			delayTime = parseInt(opts.delayTime),
			interTime = parseInt(opts.interTime),
			triggerTime = parseInt(opts.triggerTime),
			scroll = parseInt(opts.scroll),
			vis = parseInt(opts.vis),
			autoPlay = (opts.autoPlay == "false" || opts.autoPlay == false) ? false : true,
			opp = (opts.opp == "false" || opts.opp == false) ? false : true,
			autoPage = (opts.autoPage == "false" || opts.autoPage == false) ? false : true,
			pnLoop = (opts.pnLoop == "false" || opts.pnLoop == false) ? false : true,
			mouseOverStop = (opts.mouseOverStop == "false" || opts.mouseOverStop == false) ? false : true,
			defaultPlay = (opts.defaultPlay == "false" || opts.defaultPlay == false) ? false : true,
			returnDefault = (opts.returnDefault == "false" || opts.returnDefault == false) ? false : true,
			cloneDeep = (opts.cloneDeep == "false" || opts.cloneDeep == false) ? false : true,
			stepEffect = (opts.stepEffect == "false" || opts.stepEffect == false) ? false : true,
			scrollStop = (opts.scrollStop == "false" || opts.scrollStop == false) ? false : true,
			scrollStopIdx = (conBoxSize - vis),
			slideH = 0,
			slideW = 0,
			selfW = 0,
			selfH = 0,
			easing = opts.easing,
			inter = null,
			mst = null,
			rtnST = null,
			titOn = opts.titOnClassName,
			conOn = opts.conOnClassName,
			onIndex = navObj.index(slider.find("." + titOn)),
			oldIndex = index = defaultIndex = onIndex == -1 ? index : onIndex,
			_ind = index,
			cloneNum = conBoxSize >= vis ? (conBoxSize % scroll != 0 ? conBoxSize % scroll : scroll) : 0,
			_tar,
			isMarq = effect == "leftMarquee" || effect == "topMarquee" ? true : false,
			doStartFun = function () {
				if ($.isFunction(opts.startFun)) {
					opts.startFun(index, navObjSize, slider, $(opts.titCell, slider), conBox, tarObj, prevBtn, nextBtn)
				}
			},
			doEndFun = function () {
				if ($.isFunction(opts.endFun)) {
					opts.endFun(index, navObjSize, slider, $(opts.titCell, slider), conBox, tarObj, prevBtn, nextBtn)
				}
			},
			resetOn = function () {
				navObj.removeClass(titOn);
				if (defaultPlay)
					navObj.eq(defaultIndex).addClass(titOn)
			},
			hookFun = function (hookEle) {
				if (hookEle.length > 0) {
					var hookIdx = hookEle.index();
					index = scrollStopIdx <= hookIdx ? scrollStopIdx : hookIdx;
				}
			};
			if (navObjSize == 0)
				navObjSize = conBoxSize;
			if (isMarq)
				navObjSize = 2;
			if (autoPage) {
				if (conBoxSize >= vis) {
					if (effect == "leftLoop" || effect == "topLoop") {
						navObjSize = conBoxSize % scroll != 0 ? (conBoxSize / scroll^0) + 1 : conBoxSize / scroll;
					} else {
						var tempS = conBoxSize - vis;
						navObjSize = 1 + parseInt(tempS % scroll != 0 ? (tempS / scroll + 1) : (tempS / scroll));
						if (navObjSize <= 0)
							navObjSize = 1;
					}
				} else {
					navObjSize = 1
				}
				navObj.html("");
				var str = "";
				if (opts.autoPage == true || opts.autoPage == "true") {
					for (var i = 0; i < navObjSize; i++) {
						str += "<li>" + (i + 1) + "</li>"
					}
				} else {
					for (var i = 0; i < navObjSize; i++) {
						str += opts.autoPage.replace("$", (i + 1))
					}
				}
				navObj.html(str);
				var navObj = navObj.children();
			}
			if (conBoxSize > vis) {
				conBox.children().each(function () {
					if ($(this).width() > selfW) {
						selfW = $(this).width();
						slideW = $(this).outerWidth(true);
					}
					if ($(this).height() > selfH) {
						selfH = $(this).height();
						slideH = $(this).outerHeight(true);
					}
				});
				var _chr = conBox.children();
				var cloneEle = function () {
					for (var i = 0; i < vis; i++) {
						_chr.eq(i).clone(cloneDeep).addClass("clone").appendTo(conBox);
					}
					for (var i = 0; i < cloneNum; i++) {
						_chr.eq(conBoxSize - i - 1).clone(cloneDeep).addClass("clone").prependTo(conBox);
					}
				}
				hookFun(hookEle);
				switch (effect) {
				case "fold":
					conBox.css({
						"position" : "relative",
						"width" : slideW,
						"height" : slideH
					}).children().css({
						"position" : "absolute",
						"width" : selfW,
						"left" : 0,
						"top" : 0,
						"display" : "none"
					});
					break;
				case "top":
					conBox.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; height:' + vis * slideH + 'px"></div>').css({
						"top" :  - (index * scroll) * slideH,
						"position" : "relative",
						"padding" : "0",
						"margin" : "0"
					}).children().css({
						"height" : selfH
					});
					break;
				case "left":
					conBox.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; width:' + vis * slideW + 'px"></div>').css({
						"width" : conBoxSize * slideW,
						"left" :  - (index * scroll) * slideW,
						"position" : "relative",
						"overflow" : "hidden",
						"padding" : "0",
						"margin" : "0"
					}).children().css({
						"float" : "left",
						"width" : selfW
					});
					break;
				case "leftLoop":
				case "leftMarquee":
					cloneEle();
					conBox.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; width:' + vis * slideW + 'px"></div>').css({
						"width" : (conBoxSize + vis + cloneNum) * slideW,
						"position" : "relative",
						"overflow" : "hidden",
						"padding" : "0",
						"margin" : "0",
						"left" :  - (cloneNum + index * scroll) * slideW
					}).children().css({
						"float" : "left",
						"width" : selfW
					});
					break;
				case "topLoop":
				case "topMarquee":
					cloneEle();
					conBox.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; height:' + vis * slideH + 'px"></div>').css({
						"height" : (conBoxSize + vis + cloneNum) * slideH,
						"position" : "relative",
						"padding" : "0",
						"margin" : "0",
						"top" :  - (cloneNum + index * scroll) * slideH
					}).children().css({
						"height" : selfH
					});
					break;
				}
			}
			var scrollNum = function (ind) {
				var _tempCs = ind * scroll;
				if (ind == navObjSize) {
					_tempCs = conBoxSize;
				} else if (ind == -1 && conBoxSize % scroll != 0) {
					_tempCs = -conBoxSize % scroll;
				}
				return _tempCs;
			}
			var doSwitchLoad = function (objs) {
				var changeImg = function (t) {
					for (var i = t; i < (vis + t); i++) {
						objs.eq(i).find("img[" + sLoad + "]").each(function () {
							var _this = $(this);
							_this.attr("src", _this.attr(sLoad)).removeAttr(sLoad);
							if (conBox.find(".clone")[0]) {
								var chir = conBox.children();
								for (var j = 0; j < chir.size(); j++) {
									chir.eq(j).find("img[" + sLoad + "]").each(function () {
										if ($(this).attr(sLoad) == _this.attr("src"))
											$(this).attr("src", $(this).attr(sLoad)).removeAttr(sLoad)
									})
								}
							}
						})
					}
				}
				switch (effect) {
				case "fade":
				case "fold":
				case "top":
				case "left":
				case "slideDown":
					changeImg(index * scroll);
					break;
				case "leftLoop":
				case "topLoop":
					changeImg(cloneNum + scrollNum(_ind));
					break;
				case "leftMarquee":
				case "topMarquee":
					var curS = effect == "leftMarquee" ? conBox.css("left").replace("px", "") : conBox.css("top").replace("px", "");
					var slideT = effect == "leftMarquee" ? slideW : slideH;
					var mNum = cloneNum;
					if (curS % slideT != 0) {
						var curP = Math.abs(curS / slideT^0);
						if (index == 1) {
							mNum = cloneNum + curP
						} else {
							mNum = cloneNum + curP - 1
						}
					}
					changeImg(mNum);
					break;
				}
			}
			var doPlay = function (init) {
				if (defaultPlay && oldIndex == index && !init && !isMarq)
					return;
				if (isMarq) {
					if (index >= 1) {
						index = 1;
					} else if (index <= 0) {
						index = 0;
					}
				} else {
					_ind = index;
					if (index >= navObjSize) {
						index = 0;
					} else if (index < 0) {
						index = navObjSize - 1;
					}
				}
				doStartFun();
				if (sLoad != null) {
					doSwitchLoad(conBox.children())
				}
				if (tarObj[0]) {
					_tar = tarObj.eq(index);
					if (sLoad != null) {
						doSwitchLoad(tarObj)
					}
					if (effect == "slideDown") {
						tarObj.not(_tar).stop(true, true).slideUp(delayTime);
						_tar.slideDown(delayTime, easing, function () {
							if (!conBox[0])
								doEndFun()
						});
					} else {
						tarObj.not(_tar).stop(true, true).hide();
						_tar.animate({
							opacity : "show"
						}, delayTime, function () {
							if (!conBox[0])
								doEndFun()
						});
					}
				}
				if (conBoxSize >= vis) {
					hookFun($(opts.hookEle));
					switch (effect) {
					case "fade":
						conBox.children().stop(true, true).eq(index).animate({
							opacity : "show"
						}, delayTime, easing, function () {
							doEndFun()
						}).siblings().hide();
						break;
					case "fold":
						conBox.children().stop(true, true).eq(index).animate({
							opacity : "show"
						}, delayTime, easing, function () {
							doEndFun()
						}).siblings().animate({
							opacity : "hide"
						}, delayTime, easing);
						break;
					case "top":
						conBox.stop(true, false).animate({
							"top" : -index * scroll * slideH
						}, delayTime, easing, function () {
							doEndFun()
						});
						break;
					case "left":
						conBox.stop(true, false).animate({
							"left" : -index * scroll * slideW
						}, delayTime, easing, function () {
							doEndFun()
						});
						break;
					case "leftLoop":
						var __ind = _ind;
						conBox.stop(true, true).animate({
							"left" :  - (scrollNum(_ind) + cloneNum) * slideW
						}, delayTime, easing, function () {
							if (__ind <= -1) {
								conBox.css("left",  - (cloneNum + (navObjSize - 1) * scroll) * slideW);
							} else if (__ind >= navObjSize) {
								conBox.css("left", -cloneNum * slideW);
							}
							doEndFun();
						});
						break;
					case "topLoop":
						var __ind = _ind;
						conBox.stop(true, true).animate({
							"top" :  - (scrollNum(_ind) + cloneNum) * slideH
						}, delayTime, easing, function () {
							if (__ind <= -1) {
								conBox.css("top",  - (cloneNum + (navObjSize - 1) * scroll) * slideH);
							} else if (__ind >= navObjSize) {
								conBox.css("top", -cloneNum * slideH);
							}
							doEndFun();
						});
						break;
					case "leftMarquee":
						var tempLeft = conBox.css("left").replace("px", "");
						if (index == 0) {
							conBox.animate({
								"left" : ++tempLeft
							}, 0, function () {
								if (conBox.css("left").replace("px", "") >= 0) {
									conBox.css("left", -conBoxSize * slideW)
								}
							});
						} else {
							conBox.animate({
								"left" : --tempLeft
							}, 0, function () {
								if (conBox.css("left").replace("px", "") <=  - (conBoxSize + cloneNum) * slideW) {
									conBox.css("left", -cloneNum * slideW)
								}
							});
						}
						break;
					case "topMarquee":
						var tempTop = conBox.css("top").replace("px", "");
						if (index == 0) {
							conBox.animate({
								"top" : ++tempTop
							}, 0, function () {
								if (conBox.css("top").replace("px", "") >= 0) {
									conBox.css("top", -conBoxSize * slideH)
								}
							});
						} else {
							conBox.animate({
								"top" : --tempTop
							}, 0, function () {
								if (conBox.css("top").replace("px", "") <=  - (conBoxSize + cloneNum) * slideH) {
									conBox.css("top", -cloneNum * slideH)
								}
							});
						}
						break;
					}
				}
				conBox.children().removeClass(conOn).eq(index).addClass(conOn);
				navObj.removeClass(titOn).eq(index).addClass(titOn);
				oldIndex = index;
				if (!pnLoop) {
					nextBtn.removeClass("nextStop");
					prevBtn.removeClass("prevStop");
					if (index == 0) {
						prevBtn.addClass("prevStop");
					}
					if (index == navObjSize - 1) {
						nextBtn.addClass("nextStop");
					}
				}
				pageState.html("<span>" + (index + 1) + "</span>/" + navObjSize);
			};
			if (defaultPlay) {
				doPlay(true);
			}
			if (returnDefault) {
				slider.hover(function () {
                    console.log(slider);
					clearTimeout(rtnST);
				}, function () {
					rtnST = setTimeout(function () {
							index = defaultIndex;
							if (defaultPlay) {
								doPlay();
							} else {
								if (effect == "slideDown") {
									_tar.slideUp(delayTime, resetOn);
								} else {
									_tar.animate({
										opacity : "hide"
									}, delayTime, resetOn);
								}
							}
							oldIndex = index;
						}, 300);
				});
			}
			var setInter = function (time) {
				inter = setInterval(function () {
						opp ? index-- : index++;
						doPlay()
					}, !!time ? time : interTime);
			}
			var setMarInter = function (time) {
				inter = setInterval(doPlay, !!time ? time : interTime);
			}
			var resetInter = function () {
				if (!mouseOverStop) {
					clearInterval(inter);
					setInter()
				}
			}
			var nextTrigger = function () {
				if (pnLoop || index != navObjSize - 1) {
					index++;
					doPlay();
					if (!isMarq)
						resetInter();
				}
			}
			var prevTrigger = function () {
				if (pnLoop || index != 0) {
					index--;
					doPlay();
					if (!isMarq)
						resetInter();
				}
			}
			var playStateFun = function () {
				clearInterval(inter);
				isMarq ? setMarInter() : setInter();
				playState.removeClass("pauseState")
			}
			var pauseStateFun = function () {
				clearInterval(inter);
				playState.addClass("pauseState");
			}
			if (autoPlay) {
				if (isMarq) {
					opp ? index-- : index++;
					setMarInter();
					if (mouseOverStop)
						conBox.hover(pauseStateFun, playStateFun);
				} else {
					setInter();
					if (mouseOverStop)
						slider.hover(pauseStateFun, playStateFun);
				}
			} else {
				if (isMarq) {
					opp ? index-- : index++;
				}
				playState.addClass("pauseState");
			}
			playState.click(function () {
				playState.hasClass("pauseState") ? playStateFun() : pauseStateFun()
			});
			conBox.on({
				"touchstart" : function (event) {
					_startX = event.originalEvent.touches[0].clientX;
					_startY = event.originalEvent.touches[0].clientY;
					_initX = _startX;
				},
				"touchmove" : function (event) {
					var touches = event.originalEvent.touches,
					_endX = event.originalEvent.touches[0].clientX,
					_endY = event.originalEvent.touches[0].clientY,
					_absX,
					lastX;
					if (Math.abs(_endY - _startY) > Math.abs(_endX - _startX)) {
						return;
					}
					event.preventDefault();
					_finishX = _endX;
					_absX = Math.abs(_endX - _startX);
					lastX = conBox.css('left').replace('px', '');
					if (_startX > _endX) {
						pauseStateFun();
						conBox.css('left', (parseInt(lastX) - _absX) + 'px');
					} else {
						pauseStateFun();
						conBox.css('left', (parseInt(lastX) + _absX) + 'px');
					}
					_startX = _endX;
				},
				"touchend" : function (event) {
					if (_finishX == 0) {
						return;
					}
					if (_initX > _finishX) {
						bindTouch(_initX, _finishX);
					} else if (_initX < _finishX) {
						bindTouch(_initX, _finishX);
					}
					_initX = 0;
					_finishX = 0;
				}
			});
			function bindTouch(start, end) {
				if (start >= end) {
					nextTrigger();
				} else {
					prevTrigger();
				}
			};
			if (opts.trigger == "mouseover") {
				navObj.hover(function () {
					var hoverInd = navObj.index(this);
					mst = setTimeout(function () {
							index = hoverInd;
							doPlay();
							resetInter();
						}, opts.triggerTime);
				}, function () {
					clearTimeout(mst)
				});
			} else {
				navObj.click(function () {
					index = navObj.index(this);
					doPlay();
					resetInter();
				})
			}
			if (isMarq) {
				nextBtn.on("mousedown", nextTrigger);
				prevBtn.on("mousedown", prevTrigger);
				if (pnLoop) {
					var st;
					var marDown = function () {
						st = setTimeout(function () {
								clearInterval(inter);
								setMarInter(interTime / 10^0)
							}, 150)
					}
					var marUp = function () {
						clearTimeout(st);
						clearInterval(inter);
						setMarInter()
					}
					nextBtn.mousedown(marDown);
					nextBtn.mouseup(marUp);
					prevBtn.mousedown(marDown);
					prevBtn.mouseup(marUp);
				}
				if (opts.trigger == "mouseover") {
					nextBtn.hover(nextTrigger, function () {});
					prevBtn.hover(prevTrigger, function () {});
				}
			} else {
				nextBtn.click(function () {
					if (stepEffect && conBoxSize <= (index * scroll + vis)) {
						return;
					};
					nextTrigger();
					if (stepEffect && (conBoxSize <= index * scroll + vis)) {
						nextBtn.addClass("nextStop");
					}
				});
				prevBtn.click(prevTrigger);
			}
		});
	};
})(jQuery);
