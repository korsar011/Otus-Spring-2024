import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { getBookById, deleteBook } from '../services/bookService';

const BookDetail = () => {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        getBookById(id)
            .then(response => setBook(response.data))
            .catch(error => console.error('Error fetching book:', error));
    }, [id]);

    const handleDelete = () => {
        if (window.confirm('Are you sure you want to delete this book?')) {
            deleteBook(id)
                .then(() => navigate('/books'))
                .catch(error => console.error('Error deleting book:', error));
        }
    };

    return (
        <div className="container">
            {book ? (
                <>
                    <h1>{book.title}</h1>
                    <p><strong>Author:</strong> {book.author ? book.author.fullName : 'Unknown Author'}</p>
                    <p><strong>Genre:</strong> {book.genre ? book.genre.name : 'Unknown Genre'}</p>
                    <div className="button-group">
                        <Link to={`/books/${book.id}/edit`} className="button">Edit</Link>
                        <button onClick={handleDelete} className="button">Delete</button>
                    </div>
                </>
            ) : (
                <p>Loading...</p>
            )}
            <Link to="/books" className="button">Back to Books List</Link>
        </div>
    );
};

export default BookDetail;