const OPERATIONS = ['σ','∏', '∪','∩','−','X','∞','()'];

const userInput = document.getElementById("user-input");
const resultInput = document.getElementById("result-input");
const errorSection = document.getElementById("error-messages");
const errorInput = document.getElementById("errors__input");
const url = "https://algebradb.fly.dev/"
const urlLocal = "http://localhost:8080/"

document.body.addEventListener('keydown', (event) => {
    if(event.key == 'Enter') {
        getSQL()
    }
})

const addOperationToInput = (operationId) => {
    userInput.value += OPERATIONS[operationId];
    userInput.focus();
}

const setToInput = (event) => {
    userInput.value = event.srcElement.innerText;
}

const showErrorMessage = (error) => {
    errorSection.style.display = 'flex';
    errorInput.value = error;
}

const getSQL = async () => {

    resultInput.value = "";

    if(errorSection.style.display == 'flex') {
        errorSection.style.display = 'none';
    }

    if(userInput.value.length == 0) {
        return showErrorMessage("Debes de proporcionar una entrada");
    }

    try {
        const res = await postData(url, { algebra: userInput.value })
        if(res.error) {
            showErrorMessage(res.errorMessage ? res.errorMessage : "Error de sintaxis");
        } else {
            resultInput.value = res.sqlQuery;
        }
    } catch (error) {
        console.log(error);
    }
}

const toggleErrorSection = () => {
    if(errorSection.style.display == 'flex') errorSection.style.display = 'none';
    else errorSection.style.display = 'flex';
}


async function postData(url = "", data = {}) {
    const response = await fetch(url, {
      method: "POST", 
      mode: "cors", 
      cache: "no-cache", 
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      redirect: "follow", 
      referrerPolicy: "no-referrer", 
      body: JSON.stringify(data), 
    });
    
    return response.json(); 
}
