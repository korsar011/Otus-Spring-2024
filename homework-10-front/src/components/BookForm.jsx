import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const BookForm = () => {
    const { id } = useParams();
    const [book, setBook] = useState({ title: '', authorId: '', genreId: '' });
    const [authors, setAuthors] = useState([]);
    const [genres, setGenres] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('/api/authors')
            .then(response => setAuthors(response.data))
            .catch(error => console.error('Error fetching authors:', error));

        axios.get('/api/genres')
            .then(response => setGenres(response.data))
            .catch(error => console.error('Error fetching genres:', error));

        if (id) {
            axios.get(`/api/books/${id}`)
                .then(response => setBook(response.data))
                .catch(error => console.error('Error fetching book:', error));
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBook(prevBook => ({ ...prevBook, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const request = id
            ? axios.put(`/api/books/${id}`, book)
            : axios.post('/api/books', book);

        request
            .then(() => navigate('/books'))
            .catch(error => {
                setError('Error saving book.');
                console.error('Error saving book:', error);
            });
    };

    return (
        <div className="container">
            <h1>{id ? 'Edit Book' : 'Add New Book'}</h1>
            <form onSubmit={handleSubmit} className="form">
                {id && <input type="hidden" name="id" value={book.id || ''} />}
                <div className="form-group">
                    <label htmlFor="title">Title:</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={book.title || ''}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="authorId">Author:</label>
                    <select
                        id="authorId"
                        name="authorId"
                        value={book.authorId || ''}
                        onChange={handleChange}
                        className="form-control"
                        required
                    >
                        <option value="">Select Author</option>
                        {authors.map(author => (
                            <option key={author.id} value={author.id}>
                                {author.fullName}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="genreId">Genre:</label>
                    <select
                        id="genreId"
                        name="genreId"
                        value={book.genreId || ''}
                        onChange={handleChange}
                        className="form-control"
                        required
                    >
                        <option value="">Select Genre</option>
                        {genres.map(genre => (
                            <option key={genre.id} value={genre.id}>
                                {genre.name}
                            </option>
                        ))}
                    </select>
                </div>
                {error && <p className="error">{error}</p>}
                <button type="submit" className="submit-button">Save</button>
            </form>
            <div className="button-group">
                <button onClick={() => navigate('/books')} className="button">Back to Book List</button>
                <button onClick={() => navigate('/')} className="button">Home</button>
            </div>
        </div>
    );
};

export default BookForm;