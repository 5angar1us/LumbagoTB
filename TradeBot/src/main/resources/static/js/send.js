function changeAction(action) {
    if (action !== "") {
        document.getElementById("form_id").action = action;
        document.getElementById("form_id").submit();
    } else {
        throw new Exception();
    }
}

function saveSelectedValuesInHiddenInputs(){
    let hiddenInputName = "detailTradingStrategyType";
    let selectName = "detailTradingStrategyTypeSelect";

    let hiddenInputs = document.getElementsByName(hiddenInputName);
    let selects = document.getElementsByName(selectName);

    for(let i=0; i< hiddenInputs.length;i++)
    {
        console.log(hiddenInputs[i].value);
        console.log(selects[i].value);
        console.log(selects[i].options[selects[i].selectedIndex].value);
        console.log(selects[i].options[selects[i].selectedIndex].text);
        hiddenInputs[i].value = selects[i].value;
    }
}
