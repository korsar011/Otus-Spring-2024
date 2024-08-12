import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const CommentList = () => {
    const { bookId } = useParams();
    const [comments, setComments] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get(`/api/comments/by-book/${bookId}`)
            .then(response => {
                setComments(response.data);
            })
            .catch(error => {
                setError('Error fetching comments.');
                console.error('Error fetching comments:', error);
            });
    }, [bookId]);

    if (error) {
        return <div className="container"><p>{error}</p></div>;
    }

    return (
        <div className="container">
            <h1>Comments for Book {bookId}</h1>
            <div>
                {comments.length > 0 ? (
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Content</th>
                            <th>Book Title</th>
                        </tr>
                        </thead>
                        <tbody>
                        {comments.map(comment => (
                            <tr key={comment.id}>
                                <td>{comment.content}</td>
                                <td>{comment.book ? comment.book.title : 'Unknown Title'}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No comments found for this book.</p>
                )}
            </div>
            <a href={`/books`} className="button">Back to Books</a>
        </div>
    );
};

export default CommentList;