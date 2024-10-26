import React, { Component } from "react";
import { Editor } from "@tinymce/tinymce-react";

class ArticleDangerCreateWYSIWYG extends Component {
  state = {
    article: "",
    articleId: "",
  };

  createArticle = async (event) => {
    event.preventDefault();
    let response = await fetch(
      "http://localhost:8080/api/xss/danger/v1/article",
      {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify({ article: this.state.article }),
      }
    );
    let body = await response.text();
    this.setState({ articleId: body });
  };

  updateArticle = (content, editor) => {
    this.setState({ article: content });
  };

  render() {
    return (
      <div>
        <div>
          <h2>Article Danger Create</h2>
        </div>
        <Editor
          init={{
            height: 500,
            menubar: false,
            plugins: [
              "advlist autolink lists link image charmap print preview anchor",
              "insertdatetime media table paste code help wordcount save",
            ],
            toolbar:
              "undo redo | formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat | help | code",
          }}
          onEditorChange={this.updateArticle}
        />
        <button onClick={(event) => this.createArticle(event)}>Create!</button>
        <br />
        <h3>New article ID : {this.state.articleId}</h3>
      </div>
    );
  }
}

export default ArticleDangerCreateWYSIWYG;
