/// <reference path="typings/mithril/mithril.d.ts" />
var Result = app.CalculateController.Result;
var Input = app.CalculateController.Input;
function esc(value) {
    return value || value === 0 ? value : '';
}
var Controller = (function () {
    function Controller() {
        var _this = this;
        this.arg1 = m.prop(null);
        this.arg2 = m.prop(null);
        this.result = new Result();
        this.request = function () {
            if (_this.arg1() && _this.arg2()) {
                var input = new Input();
                input.arg1 = _this.arg1();
                input.arg2 = _this.arg2();
                m.request({ method: 'GET', url: '/calc', data: input, dataType: 'json' })
                    .then(function (res) { _this.result = res; });
            }
        };
    }
    return Controller;
})();
m.module(document.getElementById("calc-root"), {
    controller: function () {
        return new Controller();
    },
    view: function (ctrl) {
        return [
            'Arg1',
            m('input', { type: 'text', value: ctrl.arg1(), onchange: m.withAttr("value", ctrl.arg1) }), m('br'),
            'Arg2',
            m('input', { type: 'text', value: ctrl.arg2(), onchange: m.withAttr("value", ctrl.arg2) }), m('br'),
            m('button', { onclick: ctrl.request }, 'Calc'), m('br'),
            'Add: ' + esc(ctrl.result.add), m('br'),
            'Subtract: ' + esc(ctrl.result.subtract), m('br'),
        ];
    }
});
