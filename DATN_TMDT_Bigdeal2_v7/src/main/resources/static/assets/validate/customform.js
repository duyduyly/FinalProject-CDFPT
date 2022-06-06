// bắt lỗi bỏ trống form

(function() {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }

            form.classList.add('was-validated')
        }, false)
    })
})();

// đọc hình ảnh khi load lên
function preview() {
    anh.src = URL.createObjectURL(event.target.files[0]);
}

function preview1() {
    anh1.src = URL.createObjectURL(event.target.files[0]);
}

function preview2() {
    anh2.src = URL.createObjectURL(event.target.files[0]);
}

function preview3() {
    anh3.src = URL.createObjectURL(event.target.files[0]);
}
//sreach table
$(document).ready(function() {
	   $('#myInput').on('keyup', function(event) {
	      event.preventDefault();
	      /* Act on the event */
	      var tukhoa = $(this).val().toLowerCase();
	      $('#myTable tr').filter(function() {
	         $(this).toggle($(this).text().toLowerCase().indexOf(tukhoa)>-1);
	      });
	   });
	});