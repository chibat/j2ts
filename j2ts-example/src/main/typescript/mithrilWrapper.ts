/// <reference path="typings/mithril/mithril.d.ts" />

function BR() {
    return m('br');
}

function TEXT_FIELD(prop: (_mithril.MithrilPromiseProperty<Object>|_mithril.MithrilProperty<Object>), attr: any = {}) {
    attr.value = prop();
    attr.onchange = m.withAttr("value", prop);
    return m('input[type=text]', attr);
}

function BUTTON(value: string, onclick: (() => void), attr: any = {}) {
    attr.onclick = onclick;
    return m('button', attr, value);
}

