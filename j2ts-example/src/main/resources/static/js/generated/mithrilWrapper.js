/// <reference path="typings/mithril/mithril.d.ts" />
function $br() {
    return m('br');
}
function $input(attributes) {
    return m('input', attributes);
}
function $button(attributes, value) {
    return m('button', attributes, value);
}
