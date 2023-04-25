/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI my97M Plugin Extensions 1.0 beta
* jQuery EasyUI my97M 插件扩展
* jquery.my97M.js
* 二次开发 流云
* 最近更新：2014-05-05
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.combo.js v1.0 beta late
*   4、my97MDatePicker/WdatePicker.js
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    function create(target) {
        var state = $.data(target, "my97M"), opts = state.options,
            t = $(target).addClass("my97M-f").combo($.extend({}, opts, {
                panelWidth: 10, panelHeight: 10,
                onShowPanel: function () {
                    var box = t.combo("textbox"),
                        wopts = $.extend({}, opts, {
                            el: box[0],
                            readOnly: opts.readonly ? true : false,
                            onpicking: function (dp) {
                                if ($.isFunction(opts.onpicked) && opts.onpicked.apply(this, arguments) == false) {
                                    return false;
                                }
                                setValues(target, [dp.cal.getNewDateStr()]);
                                $.util.exec(function () { t.combo("hidePanel"); });
                            },
                            oncleared: function () { t.combo("clear"); }
                        });
                    WdatePicker.call(box[0], wopts);
                    if ($.isFunction(opts.onShowPanel)) { opts.onShowPanel.apply(this, arguments); }
                },
                onHidePanel: function () {
                    if (state.dialog) {
                        var dia = state.dialog, dopts = dia.dialog("options");
                        state.dialog = null;
                        if (!dopts.closed) { dia.dialog("close"); }
                    }
                    if ($.isFunction(opts.onHidePanel)) { opts.onHidePanel.apply(this, arguments); }
                },
                onDestroy: function () {
                    if (state.dialog) {
                        state.dialog.dialog("destroy");
                        state.dialog = null;
                    }
                    if ($.isFunction(opts.onDestroy)) { opts.onDestroy.apply(this, arguments); }
                },
                onChange: function (newValue, oldValue) {
                    if ($.isFunction(opts.onChange)) {
                        opts.onChange.apply(this, arguments);
                    }
                }
            })),
            textbox = t.combo("textbox"), panel = t.combo("panel");
        textbox.closest("span.combo").addClass("datebox");
        panel.panel("body").addClass("combo-panel-noborder");

        opts.originalValue = opts.value;
        if (opts.value) {
            setValues(target, opts.value);
        }
        t.combo("validate");
    }
    function setValues(target, values) {
        var t = $(target), opts = t.my97M("options"),
            array = $.util.likeArrayNotString(values) ? values : [values],
            text = array.join(opts.separator);
        t.combo("setValues", array).combo("setText", text);
    }
    $.fn.my97M = function (options, param) {
        if (typeof options == "string") {
            var method = $.fn.my97M.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.combo(options, param);
            }
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, "my97M");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "my97M", { options: $.extend({}, $.fn.my97M.defaults, $.fn.my97M.parseOptions(this), options) });
                create(this);
            }
        });
    };

    $.fn.my97M.parseOptions = function (target) {
        return $.extend({}, $.fn.combo.parseOptions(target), $.parser.parseOptions(target, [
            "el", "vel", "weekMethod", "lang", "skin", "dateFmt", "realDateFmt", "realTimeFmt", "realFullFmt", "minDate", "maxDate", "startDate", {
                doubleCalendar: "boolean", enableKeyboard: "boolean", enableInputMask: "boolean", autoUpdateOnChanged: "boolean",
                isShowWeek: "boolean", highLineWeekDay: "boolean", isShowClear: "boolean", isShowOK: "boolean", isShowToday: "boolean",
                isShowOthers: "boolean", autoPickDate: "boolean", qsEnabled: "boolean", autoShowQS: "boolean", opposite: "boolean"
            },
            { firstDayOfWeek: "number", errDealMode: "number" }
        ]));
    };

    $.fn.my97M.methods = {
        options: function (jq) {
            var opts = jq.combo("options"), copts = $.data(jq[0], 'my97M').options;
            return $.extend(copts, {
                originalValue: opts.originalValue, disabled: opts.disabled, readonly: opts.readonly
            });
        },

        setValues: function (jq, values) { return jq.each(function () { setValues(this, values); }); },
        setValue: function (jq, value) { return jq.each(function () { setValues(this, [value]); }); }
    };

    $.fn.my97M.defaults = $.extend({}, $.fn.combo.defaults, {
        dateFmt: "yyyy-MM"
    });

    if ($.fn.datagrid) {
        $.extend($.fn.datagrid.defaults.editors, {
            my97M: {
                init: function (container, options) {
                    var box = $("<input type=\"text\"></input>").appendTo(container).my97M(options);
                    box.my97M("textbox").addClass("datagrid-editable-input");
                    return box;
                },
                destroy: function (target) {
                    $(target).my97M("destroy");
                },
                getValue: function (target) {
                    var t = $(target), opts = t.my97M("options");
                    return t.my97M(opts.multiple ? "getValues" : "getValue");
                },
                setValue: function (target, value) {
                    var t = $(target), opts = t.my97M("options");
                    if (value) {
                        if (opts.multiple) {
                            if ($.util.likeArrayNotString(value)) {
                                t.my97M("setValues", value);
                            } else if (typeof value == "string") {
                                t.my97M("setValues", value.split(opts.separator));
                            } else {
                                t.my97M("setValue", value);
                            }
                        } else {
                            t.my97M("setValue", value);
                        }
                    } else {
                        t.my97M("clear");
                    }
                },
                resize: function (target, width) {
                    $(target).my97M("resize", width);
                },
                setFocus: function (target) {
                    $(target).my97M("textbox").focus();
                }
            }
        });
    }

    $.parser.plugins.push("my97M");

    if ($.fn.form && $.isArray($.fn.form.comboList)) {
        $.fn.form.comboList.push("my97M");
        //$.array.insert($.fn.form.comboList, 0, "my97M");
    }

})(jQuery);