import React, {Component} from 'react';

class ArticleDangerSearch extends Component {
	state = {
		query: "",
		queryCount: "",
		articles : []
	};
	
	searchTask = async (event) => {
		event.preventDefault();
		let response = await fetch('http://localhost:8080/api/xss/danger/v1/article?query=' + this.state.query);
		let body = await response.json();
		this.setState({ queryCount: body.queryCount, articles: body.result});
	}
	
	updateQuery = (event) => {
		event.preventDefault();
		this.setState({query: event.target.value});
	}
	
	render() {
		const listArticles = this.state.articles.map(
			(d) => <div key={d.articleId} dangerouslySetInnerHTML={{ __html: d.article }}/>
		);
		
		return (
			<div>
				<div><h2>Article Danger Search</h2></div>
				<input onChange={(event)=>this.updateQuery(event)} placeholder="Search article"></input>
				<button onClick={(event)=>this.searchTask(event)}>Search!</button>
				<div dangerouslySetInnerHTML={{ __html: this.state.queryCount }} />
				
				<hr/>
				
				<div>
				{listArticles}
				</div>
			</div>
		);
	}
}

export default ArticleDangerSearch;