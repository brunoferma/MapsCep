function buscarCep() {
    const cep = document.getElementById('cep').value; // Obtém o CEP do input

    // Validação do formato do CEP
    if (!/^\d{5}-?\d{3}$/.test(cep)) {
        alert('Por favor, insira um CEP válido no formato XXXXX-XXX ou XXXXXXXX.');
        return;
    }

    const formattedCep = cep.replace('-', ''); // Remove o hífen do CEP

    // Faz a requisição para o back-end para buscar o endereço
    fetch(`http://localhost:8080/cep/${formattedCep}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar o endereço');
            }
            return response.json(); // Converte a resposta para JSON
        })
        .then(data => {
            const resultadoDiv = document.getElementById('resultado');
            resultadoDiv.innerHTML = `
                <p><strong>Logradouro:</strong> ${data.logradouro}</p>
                <p><strong>Bairro:</strong> ${data.bairro}</p>
                <p><strong>Cidade:</strong> ${data.localidade}</p>
                <p><strong>UF:</strong> ${data.uf}</p>
                <p><strong>Latitude:</strong> ${data.latitude}</p>
                <p><strong>Longitude:</strong> ${data.longitude}</p>
            `;

            const mapDiv = document.getElementById('map');

            // Criando o IFrame do Google Maps com as coordenadas
            const mapIFrame = `<iframe
                width="100%"
                height="400"
                style="border: 0"
                loading="lazy"
                allowfullscreen
                src="https://www.google.com/maps/embed/v1/view?center=${data.latitude},${data.longitude}&zoom=14">
            </iframe>`;

            mapDiv.innerHTML = mapIFrame; // Adiciona o IFrame ao DOM
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao buscar o endereço: ' + error.message);
        });
}
