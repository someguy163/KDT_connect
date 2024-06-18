// 입고 버튼 클릭 시 실행되는 함수
document.querySelector('.stockbtn').addEventListener('click', function() {
    // 선택된 라디오 버튼 가져오기
    var selectedRadio = document.querySelector('input[name="productRadio"]:checked');

    if(selectedRadio) { // 라디오 버튼이 선택되었을 경우
        var selectedRow = selectedRadio.closest('tr'); // 선택된 라디오 버튼이 속한 행 가져오기

        // 선택된 행의 각 열 정보 가져오기
        var productCode = selectedRow.querySelector('th:nth-child(2)').textContent;
        var productName = selectedRow.querySelector('th:nth-child(3)').textContent;
        var orderQuantity = selectedRow.querySelector('th:nth-child(4)').textContent;
        var orderDate = selectedRow.querySelector('th:nth-child(5)').textContent;
        var deliveryDate = selectedRow.querySelector('th:nth-child(6)').textContent;
        var companyName = selectedRow.querySelector('th:nth-child(7)').textContent;

        // 선택된 행의 정보를 저장하는 코드 작성
        // 예를 들어, 아래와 같이 선택된 행의 정보를 출력하거나 원하는 동작을 수행할 수 있습니다.
        console.log("Selected Product Code: " + productCode);
        console.log("Selected Product Name: " + productName);
        console.log("Ordered Quantity: " + orderQuantity);
        console.log("Order Date: " + orderDate);
        console.log("Delivery Date: " + deliveryDate);
        console.log("Company Name: " + companyName);

        // 선택된 행의 정보를 아래의 코드로 옮겨 저장할 수 있습니다.
        // 아래 코드는 이해를 돕기 위한 예시입니다. 실제로는 원하는 동작에 맞게 수정해야 합니다.
        document.querySelector('#selectedProductTable').innerHTML = `
            <tr>
                <th class="check" scope="col"></th>
                <td>${productCode}</td>
                <td>${productName}</td>
                <td>${orderQuantity}</td>
                <td>${orderDate}</td>
                <td>${deliveryDate}</td>
                <td>${companyName}</td>
                <td><input type="number" id="receivedQuantity" style="width: 70px;"></td>
                <td></td>
            </tr>
        `;
    } else { // 라디오 버튼이 선택되지 않았을 경우
        alert("품목을 선택하세요.");
    }
});

// "저장" 버튼 클릭 시 이벤트 처리
document.getElementById('save').addEventListener('click', function() {
// 여기에 저장 기능 추가
    alert('저장되었습니다.');
});

// "삭제" 버튼 클릭 시 이벤트 처리
document.getElementById('delete').addEventListener('click', function() {
// 선택된 제품 정보를 삭제
    var selectedProductTable = document.getElementById('selectedProductTable');
    while (selectedProductTable.firstChild) {
        selectedProductTable.removeChild(selectedProductTable.firstChild);
    }
    alert('삭제되었습니다.');
});


function redirectToPublishPage() {
// 선택된 라디오 버튼 요소를 가져옴
    var selectedRadio = document.querySelector('input[name="productRadio"]:checked');

    if (selectedRadio) {
// 선택된 라디오 버튼의 값을 가져옴
        var selectedValue = selectedRadio.value;

// 선택된 값으로 URL을 만들어서 다음 페이지로 이동
        window.location.href = 'publishForm.html?selected=' + encodeURIComponent(selectedValue);
    } else {
        alert('품목을 선택해주세요!');
    }
}

// 발행 버튼에 이벤트 리스너 추가
document.getElementById('start').addEventListener('click', redirectToPublishPage);


window.onload = function() {
// 출고 버튼 클릭 시 이벤트 처리
    document.querySelector('#outbtn').addEventListener('click', function() {
        var selectedRadio = document.querySelector('input[name="productRadio"]:checked');
        if (selectedRadio) {
            var selectedRow = selectedRadio.closest('tr');
            if (selectedRow) {
                var selectedProductInfo = '';
                var productName = selectedRow.querySelector('.product-name').textContent;
                var productCode = selectedRow.querySelector('.product-code').textContent;
                var orderedQuantity = selectedRow.querySelector('.ordered-quantity').textContent;
                var orderDate = selectedRow.querySelector('.order-date').textContent;
                var deliveryDate = selectedRow.querySelector('.delivery-date').textContent;
                var supplier = selectedRow.querySelector('.supplier').textContent;
                var receivedQuantity = selectedRow.querySelector('.received-quantity').textContent;
                var receivedDate = new Date().toLocaleDateString(); // 현재 날짜를 받아옴

                selectedProductInfo += '<tr>';
                selectedProductInfo += '<td>' + productName + '</td>';
                selectedProductInfo += '<td>' + productCode + '</td>';
                selectedProductInfo += '<td>' + orderedQuantity + '</td>';
                selectedProductInfo += '<td>' + orderDate + '</td>';
                selectedProductInfo += '<td>' + deliveryDate + '</td>';
                selectedProductInfo += '<td>' + supplier + '</td>';
                selectedProductInfo += '<td>' + receivedQuantity + '</td>';
                selectedProductInfo += '<td>' + receivedDate + '</td>';
                selectedProductInfo += '<td><input type="text" placeholder="출고수량"></td>';
                selectedProductInfo += '<td><button type="button" class="deletebtn" style="margin-bottom: 10px; padding: 8px; border-style: none;">삭제</button></td>';
                selectedProductInfo += '</tr>';

                var releasedProductTable = document.querySelector('#releasedProductTable');
                if (releasedProductTable) {
                    releasedProductTable.querySelector('tbody').innerHTML += selectedProductInfo;
                }
            }
        } else {
            alert('선택된 제품이 없습니다.');
        }
    });

// 출고 리스트의 삭제 버튼 클릭 시 이벤트 처리
    document.querySelector('#releasedProductTable').addEventListener('click', function(event) {
        if (event.target.classList.contains('deletebtn')) {
            var selectedRow = event.target.closest('tr');
            if (selectedRow) {
                selectedRow.remove();
            }
        }
    });
};