"use client"

import { useEffect, useState } from 'react';
import { parse } from 'node-html-parser';
import ButtonDifficulty from './components/ButtonDifficulty'

var pageId: number = 2

const IndexPage: React.FC = () => {
	const [htmlContent, setHtmlContent] = useState<string>('');


	const fetchHtmlContent = async () => {
		try {
			pageId = pageId === 2 ? 1 : 2
			/* TODO: There is a probleme when I use the front on my mobile. Probably a problem with localhost ?
			   I wanted to use an environment variable, but it's a pain with node.js
			   Try something like this and find how to make it work:
			   const response = await fetch('http://' + process.env.BACK_ADDRESS + ":" + process.env.BACK_PORT + '/api/file/test' + pageId);
			*/

			const response = await fetch('http://localhost:8080/api/file/test' + pageId);
			const data = await response.text(); // Receive response as text
			setHtmlContent(data);
		} catch (error) {
			console.error('Error fetching HTML content:', error);
		}
	};

	useEffect(() => {
		fetchHtmlContent();
	}, []);

	const extractQuestion = (content: string) => {
		const root = parse(content);
		const questionDiv = root.querySelector('#question');
		if (questionDiv) {
			return questionDiv.outerHTML;
		}
		return '';
	};

	const extractAnswer = (content: string) => {
		const root = parse(content);
		const answerDiv = root.querySelector('#answer');
		if (answerDiv) {
			return answerDiv.outerHTML;
		}
		return '';
	};

	const refreshContent = () => {
		fetchHtmlContent();
	};

	return (
		<div>
			<main>
				<div className="flex flex-col justify-center items-center h-screen space-y-4">
					<div className="flex justify-left w-1/2">
						<h1 className="text-3xl justify-left font-bold mb-1">Hello, World!</h1>
					</div>
					<div className="w-1/2 p-6 bg-gray-700 rounded-lg shadow-lg">
						<div dangerouslySetInnerHTML={{ __html: extractQuestion(htmlContent) }} />
						<hr className="h-px my-4 bg-gray-500 border-0"></hr>
						<div dangerouslySetInnerHTML={{ __html: extractAnswer(htmlContent) }} />

					</div>

					<div className="flex justify-left w-1/2">
						<ButtonDifficulty onClick={refreshContent} color="button-difficulty-green">Easy</ButtonDifficulty>
						<ButtonDifficulty onClick={refreshContent} color="button-difficulty-red">Next</ButtonDifficulty>
					</div>
				</div>
			</main>
		</div>
	);

};

export default IndexPage;