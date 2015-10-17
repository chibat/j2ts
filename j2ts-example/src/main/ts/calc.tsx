/// <reference path="typings/tsd.d.ts" />

import Result = app.CalculateController.Result;
import Input = app.CalculateController.Input;

function esc(value: number | string) {
    return value || value === 0 ? value : '';
}

function value(e: React.FormEvent) {
    return (e.target as any).value;
}

class MyComponent extends React.Component<{}, {arg1?: string, arg2?: string, result?: Result}> {
    constructor() {
        super();
        this.state = {arg1: '', arg2: '', result: new Result()};
    }
    request = (e: React.SyntheticEvent) => {
        e.preventDefault(); // 処理後にsubmitしない
        if (this.state.arg1 && this.state.arg2) {
            const input = new Input();
            input.arg1 = Number(this.state.arg1);
            input.arg2 = Number(this.state.arg2);
            $.ajax({
                method: 'GET',
                url: '/calc',
                data: input,
                dataType: 'json',
                success: (data: Result, textStatus: string, jqXHR: JQueryXHR) => {
                    this.setState({result: data});
                }
            });
        }
    }
    render() {
        return (
<form className="form-horizontal" onSubmit={this.request}>
  <div className="form-group">
    <label htmlFor="arg1" className="col-sm-2 control-label">Arg1</label>
    <div className="col-sm-5">
      <input type="text" className="form-control" placeholder="Arg1" value={this.state.arg1} onChange={e=>this.setState({arg1: value(e)})} />
    </div>
  </div>
  <div className="form-group">
    <label htmlFor="arg2" className="col-sm-2 control-label">Arg2</label>
    <div className="col-sm-5">
      <input type="text" className="form-control" placeholder="Arg2" value={this.state.arg2} onChange={e=>this.setState({arg2: value(e)})} />
    </div>
  </div>
  <div className="form-group">
    <div className="col-sm-offset-2 col-sm-10">
      <button className="btn btn-default">Calc</button>
    </div>
  </div>
  <div className="form-group">
    <div className="col-sm-offset-2 col-sm-10">
    Add: {esc(this.state.result.add)}<br/>
    Subtract: {esc(this.state.result.subtract)}
    </div>
  </div>
</form>
        );
    }
}

React.render(<MyComponent />, document.getElementById('example'));




