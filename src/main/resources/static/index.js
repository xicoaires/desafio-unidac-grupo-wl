/**
 * Nome do arquivo: index.js
 * Autor: Francisco Aires
 * Data de criação: 21/04/2023
 * Descrição: Javascript da tela principal
 */

/*
A função fetchData é responsável por buscar dados dos colaboradores na base e preencher uma
tabela com esses dados, bem como criar botões para excluir dados e seletores para confirmar a
participação do colaborador no café da manhã.
*/
async function fetchData() {
    try {
        const response = await fetch('/employees');
        const data = await response.json();
        data.sort((a, b) => new Date(a.date) - new Date(b.date));

        const tableBody = document.getElementById('table-body');
        data.forEach((row) => {
            const tr = document.createElement('tr');
            tr.dataset.name = row.name;
            tr.dataset.cpf = row.cpf;
            tr.dataset.foodOption = row.foodOption;
            tr.dataset.date = row.date;

            const tdNome = document.createElement('td');
            tdNome.textContent = row.name;
            tr.appendChild(tdNome);

            const tdCPF = document.createElement('td');
            tdCPF.textContent = row.cpf;
            tr.appendChild(tdCPF);

            const tdCafe = document.createElement('td');
            tdCafe.textContent = row.foodOption;
            tr.appendChild(tdCafe);

            const tdData = document.createElement('td');
            rowDate = row.date.replace(/T.*/,'').split('-').reverse().join('/');
            tdData.textContent = rowDate
            tr.appendChild(tdData);

            const tdProduto = document.createElement('td');

            const select = document.createElement('select');

            const optionSelecione = document.createElement('option');
            optionSelecione.value = 'Selecione';
            optionSelecione.textContent = 'Selecione';
            select.appendChild(optionSelecione);

            const optionSim = document.createElement('option');
            optionSim.value = 'Sim';
            optionSim.textContent = 'Sim';
            select.appendChild(optionSim);

            const optionNao = document.createElement('option');
            optionNao.value = 'Não';
            optionNao.textContent = 'Não';
            select.appendChild(optionNao);

            if (row.verification === true) {
                select.value = 'Sim';
            } else if (row.verification === false) {
                select.value = 'Não';
            } else {
                select.value = 'Selecione';
            }

            tr.classList.add(getClassByVerification(row.verification));

            tdProduto.appendChild(select);
            tr.appendChild(tdProduto);

            const tdAcoes = document.createElement('td');

            const deleteButton = document.createElement('button');
            deleteButton.classList.add('ui', 'icon', 'button');
            const deleteIcon = document.createElement('i');
            deleteIcon.classList.add('trash', 'icon');
            deleteButton.appendChild(deleteIcon);
            deleteButton.addEventListener('click', () => {
              if (confirm('Tem certeza que deseja excluir essa linha?')) {
                deleteRow(row.id);
            }
            });
            tdAcoes.appendChild(deleteButton);

            tr.appendChild(tdAcoes);

            select.addEventListener('change', (event) => {
                const verification = event.target.value === 'Sim';
                const tr = event.target.closest('tr');
                const name = tr.dataset.name;
                const cpf = tr.dataset.cpf;
                const foodOption = tr.dataset.foodOption;
                const date = tr.dataset.date;

                updateVerification(row.id, verification, name, cpf, foodOption, date);
                location.reload()
            });

            let today = new Date();
            todayString = today.toISOString().split('T')[0];
            let day = new Date(row.date);
            dayString  = day.toISOString().split('T')[0];
            if (dayString !== todayString) {
                tr.classList.add('disable');
                select.disabled = true;
                deleteButton.disabled = false;
              }
            tableBody.appendChild(tr);

        });
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
}

fetchData();

// Função para atualizar o campo se a pessoa trouxe ou não o item
function updateVerification(id, verification, name, cpf, foodOption, date) {
    const xhr = new XMLHttpRequest();
    xhr.open('PUT', `/employees/${id}`, true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    xhr.send(JSON.stringify({ verification, name, cpf, foodOption, date }));
}

//Função para adicionar a classe com a cor da linha para verde quando o colaborador trouxer o item e vermelho se não trouxer.
function getClassByVerification(verification) {
    if (verification === true) {
        return 'positive';
    } else if (verification === false) {
        return 'error';
    } else {
        return 'table-secondary';
    }
}

/*
Função responsável por adicionar um novo registro de colaborador e suas opções de café da manhã na tabela.
Ela verifica se o nome ou CPF já foram escolhidos por outros colaboradores ou se as opções escolhidas já foram selecionadas,
e se tudo estiver correto, adiciona o registro na tabela.
*/
async function addItem(name, cpf, foodOption, date) {
        let response = await fetch('/employees');
        const data = await response.json();

        const foundName = data.find((row) => row.name === name);
        if (foundName) {
            alert(`O colaborador "${name}" já escolheu os itens.`);
            return;
        }

        const foundCpf = data.find((row) => row.cpf === cpf);
        if (foundCpf) {
            alert(`O colaborador com o CPF nº ${cpf} já escolheu o item.`);
            return;
        }

        const foodOptions2 = foodOption.split(',');

        for (const food of foodOptions2) {
            const foundFoods = data.some((row) => row.foodOption.includes(food));
            if (foundFoods) {
              alert(`O item "${food}" já foi escolhido.`);
              return;
            }
        }
    
        response = await fetch('/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: name, cpf: cpf, foodOption: foodOption, date: date })
        });

        await response.json();

        console.log('Registro adicionado:', data);

        // atualiza a tabela
        location.reload();
}

// Função para abrir o modal de adicionar novo registro
function newItemModal() {
    $('.special.modal')
        .modal({
            centered: false
        })
        .modal('show')
};

/*
Função responsável por definir o comportamento do botão "Salvar" no modal de adicionar itens ao sistema.
Quando o botão é clicado, a função obtém os valores dos campos do formulário, verifica se todos os campos
foram preenchidos e, se sim, chama a função addItem para adicionar o novo item.
Caso contrário, a função adiciona a classe 'error' aos campos vazios, para indicar que são obrigatórios.
*/
function initialize() {
    // define o comportamento do botão "Salvar" no modal de adicionar
    const saveItemButton = document.getElementById("btn-save-item");
    saveItemButton.addEventListener('click', function () {
        // obtém os valores dos campos do formulário
        const name = $('#name').val();
        const cpf = $('#cpf').val();
        const foodOption = $('#foodOption').val();
        const date = $('#date').val();

        // verifica se todos os campos estão preenchidos
        if (name && cpf && foodOption && date) {
            addItem(name, cpf, foodOption, date);

        } else {
            if ($('#name').val() === '') {
                $('#input-name').addClass('error');
            }

            if ($('#cpf').val() === '') {
                $('#input-cpf').addClass('error');
            }

            if ($('#foodOption').val() === '') {
                $('#input-foodOption').addClass('error');
            }

            if ($('#date').val() === '') {
                $('#input-date').addClass('error');
            } 
        }
    })
}

/*
Função responsável por adicionar validações de formulário. Monitora as alterações dos campos e adiciona
uma classe CSS de erro se algum deles estiver vazio ou se o CPF tiver menos de 11 caracteres.
A função também chama a função initialize(), que define o comportamento do botão "Salvar"
no modal de adicionar. A função define o valor mínimo permitido para a data como amanhã.
*/
window.addEventListener('load', function () {
    $('#name').change(function () {
        if ($('#name').val() === '') {
            $('#input-name').addClass('error');
        } else {
            $('#input-name').removeClass('error');
        }
    });

    $('#cpf').change(function () {
        if (($('#cpf').val() === '') || (($('#cpf').val()).length < 11)){
            $('#input-cpf').addClass('error');
        } else {
            $('#input-cpf').removeClass('error');
        }
    });

    $('#foodOption').change(function () {
        if ($('#foodOption').val() === '') {
            $('#input-foodOption').addClass('error');
        } else {
            $('#input-foodOption').removeClass('error');
        }
    });

    $('#date').change(function () {
        if ($('#date').val() === '') {
            $('#input-date').addClass('error');
        } else {
            $('#input-date').removeClass('error');
        }
    });

    initialize()

    let tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const tomorrowString = tomorrow.toISOString().split('T')[0];
    $("#date").attr('min', tomorrowString);
    $('#input-date').attr("min", tomorrowString);
});
/*
Função faz uma requisição assíncrona para deletar uma linha de dados do servidor.
Utiliza o método HTTP "DELETE" e aguarda a resposta da requisição com o comando "await".
Quando a requisição é bem-sucedida, a página é recarregada para atualizar a tabela exibida.
*/
async function deleteRow(id) {
      const response = await fetch(`/employees/${id}`, {
        method: 'DELETE',
      });

      location.reload()  }

