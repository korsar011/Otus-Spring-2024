import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {deleteBook, getBooks} from '../services/bookService';
import { getAuthorById } from '../services/authorService';
import { getGenreById } from '../services/genreService';

const BookList = () => {
    const [books, setBooks] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        getBooks()
            .then(response => {
                const bookPromises = response.data.map(book =>
                    Promise.all([
                        getAuthorById(book.authorId),
                        getGenreById(book.genreId)
                    ])
                        .then(([authorResponse, genreResponse]) => ({
                            ...book,
                            author: authorResponse.data,
                            genre: genreResponse.data
                        }))
                );

                Promise.all(bookPromises)
                    .then(booksWithDetails => setBooks(booksWithDetails))
                    .catch(error => {
                        setError('Error fetching books.');
                        console.error('Error fetching books:', error);
                    });
            })
            .catch(error => {
                setError('Error fetching books.');
                console.error('Error fetching books:', error);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm('Are you sure you want to delete this book?')) {
            deleteBook(id)
                .then(() => setBooks(books.filter(b => b.id !== id)))
                .catch(error => console.error('Error deleting book:', error));
        }
    };

    if (error) {
        return <div className="container"><p>{error}</p></div>;
    }

    return (
        <div className="container">
            <h1>Books List</h1>
            <table className="data-table">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th>Comments</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {books.length > 0 ? (
                    books.map(book => (
                        <tr key={book.id}>
                            <td>{book.title}</td>
                            <td>{book.author ? book.author.fullName : 'Unknown Author'}</td>
                            <td>{book.genre ? book.genre.name : 'Unknown Genre'}</td>
                            <td>
                                <Link to={`/comments/${book.id}`} className="button">View Comments</Link>
                            </td>
                            <td className="action-buttons">
                                <Link to={`/books/${book.id}/edit`} className="edit-button">Edit</Link>
                                <button
                                    onClick={() => handleDelete(book.id)}
                                    className="delete-button"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="5">No books available.</td>
                    </tr>
                )}
                </tbody>
            </table>
            <div className="button-group">
                <Link to="/books/new" className="button">Add New Book</Link>
                <Link to="/" className="button">Home</Link>
            </div>
        </div>
    );
};

export default BookList;