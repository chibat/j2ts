/// <reference path="typings/mithril/mithril.d.ts" />

function $br() {
    return m('br');
}

function $input(attributes: {type: string; value: (string|number); onchange: ((value: any) => void)}) {
    return m('input', attributes);
}

function $button(attributes: {onclick: (() => void)}, value: string) {
    return m('button', attributes, value);
}

