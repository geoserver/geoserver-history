/*
 * $Id: DirectPage.java 4206 2006-02-08 19:51:54Z ivaynberg $
 * $Revision: 4206 $
 * $Date: 2006-02-08 20:51:54 +0100 (Wed, 08 Feb 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.spring.direct.web;

import wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import wicket.spring.common.web.ContactsDisplayPage;


public class DirectPage extends ContactsDisplayPage {

	public DirectPage() {

	}

	protected SortableDataProvider getDataProvider() {
		return new DirectDataProvider();
	}
}
