/// <reference path="typings/tsd.d.ts" />

import Result = app.CalculateController.Result;
import Input = app.CalculateController.Input;

function esc(value: number | string) {
    return value || value === 0 ? value : '';
}

class Controller {
    arg1 = m.prop<number>(null);
    arg2 = m.prop<number>(null);
    result = new Result();
    request = () => {
        if (this.arg1() && this.arg2()) {
            const input = new Input();
            input.arg1 = this.arg1();
            input.arg2 = this.arg2();
            m.request<Result>({method: 'GET', url: '/calc', data: input, dataType: 'json'})
                .then((res)=>{this.result = res;});
        }
    }
}

m.module(document.getElementById("calc-root"), {
    controller: () => {
        return new Controller();
    },
    view: (ctrl: Controller) => {
        return [
            'Arg1',
            m('input',{type: 'text', value: ctrl.arg1(), onchange: m.withAttr("value", ctrl.arg1)}), m('br'),
            'Arg2',
            m('input', {type: 'text', value: ctrl.arg2(), onchange: m.withAttr("value", ctrl.arg2)}), m('br'),
            m('button', {onclick: ctrl.request}, 'Calc'), m('br'),
            'Add: ' + esc(ctrl.result.add), m('br'),
            'Subtract: ' + esc(ctrl.result.subtract), m('br'),
        ];
    }
});


