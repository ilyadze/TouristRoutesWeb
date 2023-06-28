// Get the HTML elements
const rating = document.querySelector('.rating');
const commentForm = document.querySelector('#comment-form');
const commentInput = document.querySelector('#comment');
const commentsDiv = document.querySelector('#comments');
const totalRating = document.querySelector('#total-rating');
const totalComments = document.querySelector('#total-comments');

// Initialize the comments and rating variables
let comments = [];
let ratingValue = 0;

// Add event listener to the rating stars
rating.addEventListener('change', () => {
    ratingValue = document.querySelector('input[name="rating"]:checked').value;
    totalRating.textContent = `Total Rating: ${ratingValue}`;
    console.log(`You selected ${ratingValue} stars`);
});

// Add event listener to the comment form
// commentForm.addEventListener('submit', (e) => {
//     e.preventDefault();
//     const commentValue = commentInput.value.trim();
//     if (commentValue) {
//         // Create a new comment object
//         const newComment = {
//             text: commentValue,
//             timestamp: new Date().toISOString(),
//         };
//         // Add the new comment to the comments array
//         comments.push(newComment);
//         // Reset the comment form
//         commentInput.value = '';
//         // Render the comments
//         renderComments();
//         // Update the total comments count
//         totalComments.textContent = `Total Comments: ${comments.length}`;
//     }
// });

// Render the comments
// function renderComments() {
//     commentsDiv.innerHTML = '';
//     comments.forEach((comment) => {
//         const commentDiv = document.createElement('div');
//         commentDiv.classList.add('comment');
//         const commentHeader = document.createElement('h4');
//         commentHeader.textContent = new Date(comment.timestamp).toLocaleString();
//         const commentBody = document.createElement('p');
//         commentBody.textContent = comment.text;
//         commentDiv.appendChild(commentHeader);
//         commentDiv.appendChild(commentBody);
//         commentsDiv.appendChild(commentDiv);
//     });
// }